package com.taskail.googleplacessearchdialog

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 *Created by ed on 1/21/18.
 */

class GoogleAutoCompletePredictions(
        private val googleApiClient: GoogleApiClient,
        private val latLngBounds: LatLngBounds,
        private val context: Context) {

    fun getAutocompletePredictions(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        if (googleApiClient.isConnected) {
            Log.i("DialogAdapter", "Starting autocomplete query for: " + constraint)

            val filter = AutocompleteFilter
                    .Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .build()

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            val results = Places.GeoDataApi
                    .getAutocompletePredictions(googleApiClient, constraint.toString(),
                            latLngBounds, filter)

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            val autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS)

            // Confirm that the query completed successfully, otherwise return null
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                Toast.makeText(context, "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show()
                Log.e("DialogAdapter", "Error getting autocomplete prediction API call: " + status.toString())
                autocompletePredictions.release()
                return null
            }

            Log.i("DialogAdapter", "Query completed. Received " + autocompletePredictions.count
                    + " predictions.")

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions)
        }
        Log.e("DialogAdapter", "Google API client is not connected for autocomplete query.")
        return null
    }
}