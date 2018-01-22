package com.taskail.simpleplacessearchdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog
import com.taskail.googleplacessearchdialog.PlacesSearchDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val MT_VIEW_CALIFORNIA = LatLngBounds(LatLng(37.398160, -122.180831), LatLng(37.430610, -121.972090))


        locationBtn.setOnClickListener {
            PlacesSearchDialogBuilder(this)
                    //Filter for cities only
                    .setResultsFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    // enter a custom hint
                    .setSearchHint("My Custom Hint")
                    // enter bounds, Mountain View California in this case
                    .setLatLngBounds(MT_VIEW_CALIFORNIA)
                    //set the place selected callback
                    .setLocationListener(object : SimplePlacesSearchDialog.PlaceSelectedCallback {
                        override fun onPlaceSelected(place: Place) {

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
