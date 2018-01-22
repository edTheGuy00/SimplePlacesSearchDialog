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
class PlacesAdapter(private val context: Context,
                    private val googleApiClient: GoogleApiClient,
                    private val latLngBounds: LatLngBounds,
                    private val placeFilter: AutocompleteFilter) :
        RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    private val STYLE_BOLD = StyleSpan(Typeface.BOLD)
    private lateinit var mResultList: ArrayList<AutocompletePrediction>

    class PlacesViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView){



    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlacesViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(android.R.layout.simple_expandable_list_item_2,
                        parent, false)

        return PlacesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return mResultList.size
    }

}