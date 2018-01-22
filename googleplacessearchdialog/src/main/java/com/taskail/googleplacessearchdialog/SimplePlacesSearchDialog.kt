package com.taskail.googleplacessearchdialog

import android.content.Context
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
open class SimplePlacesSearchDialog(private val context: Context) {

    var latLngBounds: LatLngBounds? = null
    var locationSelectedListener: SearchDialog.LocationSelectedCallback? = null

    fun setLatLngBounds(latLngBounds: LatLngBounds) : SimplePlacesSearchDialog{
        this.latLngBounds = latLngBounds
        return this
    }

    fun setLocationListener(locationSelectedCallback: SearchDialog.LocationSelectedCallback) :
            SimplePlacesSearchDialog{
        this.locationSelectedListener = locationSelectedCallback
        return this
    }

    fun build() : SearchDialog{
        return SearchDialog(context, this)
    }
}