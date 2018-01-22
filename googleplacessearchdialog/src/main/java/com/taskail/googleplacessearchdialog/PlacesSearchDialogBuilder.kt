package com.taskail.googleplacessearchdialog

import android.content.Context
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
open class PlacesSearchDialogBuilder(private val context: Context) {

    var latLngBounds: LatLngBounds? = null
    var locationSelectedListener: SimplePlacesSearchDialog.LocationSelectedCallback? = null

    fun setLatLngBounds(latLngBounds: LatLngBounds) : PlacesSearchDialogBuilder {
        this.latLngBounds = latLngBounds
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