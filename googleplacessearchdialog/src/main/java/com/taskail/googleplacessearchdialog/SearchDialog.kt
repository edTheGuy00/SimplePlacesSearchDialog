package com.taskail.googleplacessearchdialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
class SearchDialog(context: Context,
                   private val builder: SimplePlacesSearchDialog) :
        AppCompatDialog(context) {

    // Bounds of the world
    private var BOUNDS_WORLD = LatLngBounds(LatLng(-85.0, 180.0), LatLng(85.0, -180.0))

    interface LocationSelectedCallback{
        fun onLocationSelected(locationName: String,
                               locationLat: Double,
                               locationLng: Double)
    }

    init {
        setContentView(R.layout.dialog_simple_search)
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val background : View? = findViewById(R.id.touchable_background)
        background?.setOnClickListener {
            dismiss()
        }


    }
}