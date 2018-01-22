package com.taskail.simpleplacessearchdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog
import com.taskail.googleplacessearchdialog.PlacesSearchDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationBtn.setOnClickListener {
            PlacesSearchDialogBuilder(this)
                    .setLocationListener(object : SimplePlacesSearchDialog.LocationSelectedCallback{
                        override fun onLocationSelected(place: Place) {

                            placeName.text = place.name
                            fullAddress.text = place.address
                            lat.text = place.latLng.latitude.toString()
                            lng.text = place.latLng.longitude.toString()
                        }

                    })
                    .build()
                    .show()
        }
    }
}
