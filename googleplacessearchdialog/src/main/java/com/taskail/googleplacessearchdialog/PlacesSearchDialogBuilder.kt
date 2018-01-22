package com.taskail.googleplacessearchdialog

import android.content.Context
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
open class PlacesSearchDialogBuilder(private val context: Context) {

    private val CITIES = AutocompleteFilter.TYPE_FILTER_CITIES
    private val ESTABLISHMENTS = AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT
    private val ADDRESSES = AutocompleteFilter.TYPE_FILTER_ADDRESS
    private val REGION = AutocompleteFilter.TYPE_FILTER_REGIONS

    private var searchFilterType = AutocompleteFilter.TYPE_FILTER_NONE

    internal var searchHint: String = context.getString(R.string.search_hint)
    internal var latLngBounds: LatLngBounds? = null
    internal var locationSelectedListener: SimplePlacesSearchDialog.LocationSelectedCallback? = null
    internal var searchFilter: AutocompleteFilter = AutocompleteFilter.Builder().setTypeFilter(searchFilterType).build()


    fun setLatLngBounds(latLngBounds: LatLngBounds) : PlacesSearchDialogBuilder {
        this.latLngBounds = latLngBounds
        return this
    }

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    fun setResultsFilter(filter: Int): PlacesSearchDialogBuilder{

        when(filter) {
            CITIES -> this.searchFilterType = CITIES

            ESTABLISHMENTS -> this.searchFilterType = ESTABLISHMENTS

            ADDRESSES -> this.searchFilterType = ADDRESSES

            REGION -> this.searchFilterType = REGION

            else -> this.searchFilterType = AutocompleteFilter.TYPE_FILTER_NONE
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