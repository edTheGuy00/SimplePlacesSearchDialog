package com.taskail.googleplacessearchdialog

import com.google.android.gms.maps.model.LatLng

/**
 *Created by ed on 1/21/18.
 */
interface PlaceCallback {
    fun onPlaceSelected(placeName: String, latLng: LatLng)
    fun onError()
}