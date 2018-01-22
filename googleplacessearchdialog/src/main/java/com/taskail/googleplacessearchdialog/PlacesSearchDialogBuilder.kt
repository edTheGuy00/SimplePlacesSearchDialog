package com.taskail.googleplacessearchdialog

import android.content.Context
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
open class PlacesSearchDialogBuilder(private val context: Context) {

    val CITIES = AutocompleteFilter.TYPE_FILTER_CITIES
    val ESTABLISHMENTS = AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT
    val ADDRESSES = AutocompleteFilter.TYPE_FILTER_ADDRESS

    var latLngBounds: LatLngBounds? = null
    var locationSelectedListener: SimplePlacesSearchDialog.LocationSelectedCallback? = null
    var searchFilter: AutocompleteFilter = AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()

    fun setLatLngBounds(latLngBounds: LatLngBounds) : PlacesSearchDialogBuilder {
        this.latLngBounds = latLngBounds
        return this
    }

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    fun setResultsFilter(filter: Int): PlacesSearchDialogBuilder{

        when(filter) {
            CITIES -> this.searchFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(CITIES).build()

            ESTABLISHMENTS -> this.searchFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build()

            ADDRESSES -> this.searchFilter = AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build()
        }

        return this
    }

    fun setLocationListener(locationSelectedCallback: SimplePlacesSearchDialog.LocationSelectedCallback) :
            PlacesSearchDialogBuilder {
        this.locationSelectedListener = locationSelectedCallback
        return this
    }

    fun build() : SimplePlacesSearchDialog {
        return SimplePlacesSearchDialog(context, this)
    }
}