/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.taskail.googleplacessearchdialog

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 * <p>
 * Note that this adapter requires a valid {@link com.google.android.gms.common.api.GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 */
class PlaceAutocompleteAdapter(private val context: Context,
                               private val googleApiClient: GoogleApiClient,
                               private val latLngBounds: LatLngBounds,
                               private val callback: PlaceCallback,
                               private val  mPlaceFilter: AutocompleteFilter) :
        RecyclerView.Adapter<PlaceAutocompleteAdapter.PlacesViewHolder>(), Filterable {

    private val TAG = "PlaceAutoAdapter"
    private val STYLE_BOLD = StyleSpan(Typeface.BOLD)
    /**
     * Current results returned by this adapter.
     */
    private var mResultList: ArrayList<AutocompletePrediction>? = null

    class PlacesViewHolder(itemView: View,
                           val callback: PlaceCallback,
                           val googleApiClient: GoogleApiClient) :
            RecyclerView.ViewHolder(itemView){

        val primaryText = itemView.rootView.findViewById<TextView>(R.id.text1)
        val secondaryText = itemView.rootView.findViewById<TextView>(R.id.text2)

        fun setPlace(primary: String, secondary: String, placeId: String){
            primaryText.text = primary
            secondaryText.text = secondary

            itemView.setOnClickListener {
                getLatLng(placeId)
            }
        }

        private fun getLatLng(placeId: String){
            val placeBufferResults : PendingResult<PlaceBuffer> = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId)

            placeBufferResults.setResultCallback {
                if (it.count == 1){
                    callback.onPlaceSelected(it[0])
                } else {
                    callback.onError()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceAutocompleteAdapter.PlacesViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.custom_expadable_list_item,
                        parent, false)

        return PlaceAutocompleteAdapter.PlacesViewHolder(itemView, callback, googleApiClient)
    }

    override fun onBindViewHolder(holder: PlaceAutocompleteAdapter.PlacesViewHolder?, position: Int) {
        holder?.setPlace(getPrimaryText(position), getSecondaryText(position), getPlaceId(position))

    }

    private fun getPrimaryText(position: Int) : String{
        return mResultList!![position].getPrimaryText(STYLE_BOLD).toString()
    }

    private fun getSecondaryText(position: Int) : String{
        return mResultList!![position].getSecondaryText(STYLE_BOLD).toString()
    }

    private fun getPlaceId(position: Int) : String {
       return mResultList!![position].placeId!!
    }

    override fun getItemCount(): Int {
        return if (mResultList != null) {
            mResultList?.size!!
        } else
            0
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                callback.onLoading(true)

                val results = Filter.FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size

                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults?) {

                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                    callback.resultsReturned()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetChanged()
                    callback.onNoResultsReturned()
                }

                callback.onLoading(false)
            }

            override fun convertResultToString(resultValue: Any): CharSequence {

                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                return if (resultValue is AutocompletePrediction) {
                    resultValue.getFullText(null)
                } else {
                    super.convertResultToString(resultValue)
                }
            }
        }
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * objects to store the Place ID and description that the API returns.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see Places.GEO_DATA_API.getAutocomplete
     * @see AutocompletePrediction.freeze
     */
    private fun getAutocomplete(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        if (googleApiClient.isConnected) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint)

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            val results = Places.GeoDataApi
                    .getAutocompletePredictions(googleApiClient, constraint.toString(),
                            latLngBounds, mPlaceFilter)


            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            val autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS)

            // Confirm that the query completed successfully, otherwise return null
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                Toast.makeText(context, "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show()

                status.statusCode
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString())
                autocompletePredictions.release()
                return null
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.count
                    + " predictions.")

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions)
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.")
        return null
    }
}