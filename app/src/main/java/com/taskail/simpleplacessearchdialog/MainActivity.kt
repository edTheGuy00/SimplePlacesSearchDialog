package com.taskail.simpleplacessearchdialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.taskail.googleplacessearchdialog.SearchDialog
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationBtn.setOnClickListener {
            SimplePlacesSearchDialog(this)
                    .setLocationListener(object : SearchDialog.LocationSelectedCallback{
                        override fun onLocationSelected(locationName: String,
                                                        locationLat: Double, locationLng: Double) {

                        }

                    })
                    .build()
                    .show()
        }
    }
}
