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

    internal var searchFilterType: Int? = null
    internal var searchHint: String = context.getString(R.string.search_hint)
    internal var customSearchHint: String? = null
    internal var latLngBounds: LatLngBounds? = null
    internal var locationSelectedListener: SimplePlacesSearchDialog.PlaceSelectedCallback? = null


    fun setLatLngBounds(latLngBounds: LatLngBounds) : PlacesSearchDialogBuilder {
        this.latLngBounds = latLngBounds
        return this
    }

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    fun setResultsFilter(filter: Int): PlacesSearchDialogBuilder{

        when(filter) {
            CITIES -> {
                this.searchFilterType = CITIES
                searchHint = context.getString(R.string.search_city_hint)
            }


            ESTABLISHMENTS -> {
                this.searchFilterType = ESTABLISHMENTS
                searchHint = context.getString(R.string.search_establishment_hint)
            }

            ADDRESSES -> {
                this.searchFilterType = ADDRESSES
                searchHint = context.getString(R.string.search_address_hint)
            }

            REGION -> this.searchFilterType = REGION

            else -> this.searchFilterType = AutocompleteFilter.TYPE_FILTER_NONE
        }

        return this
    }

    fun setSearchHint(hint: String): PlacesSearchDialogBuilder{
        this.customSearchHint = hint
        return this
    }

    fun setLocationListener(locationSelectedCallback: SimplePlacesSearchDialog.PlaceSelectedCallback) :
            PlacesSearchDialogBuilder {
        this.locationSelectedListener = locationSelectedCallback
        return this
    }

    fun build() : SimplePlacesSearchDialog {
        return SimplePlacesSearchDialog(context, this)
    }
}