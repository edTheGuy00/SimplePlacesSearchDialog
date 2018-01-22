package com.taskail.googleplacessearchdialog

import com.google.android.gms.location.places.Place

/**
 *Created by ed on 1/21/18.
 */
interface PlaceCallback {

    fun onLoading(showLoading: Boolean)
    fun onPlaceSelected(place: Place)
    fun onNoResultsReturned()
    fun resultsReturned()
    fun onError()
}