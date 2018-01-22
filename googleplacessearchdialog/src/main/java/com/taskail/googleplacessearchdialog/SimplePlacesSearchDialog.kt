package com.taskail.googleplacessearchdialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.util.Log
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
class SimplePlacesSearchDialog(context: Context,
                               private val builder: PlacesSearchDialogBuilder) :
        AppCompatDialog(context), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    val tag = "Places Search Dialog"
    // Bounds of the world
    private var BOUNDS_WORLD = LatLngBounds(LatLng(-85.0, 180.0), LatLng(85.0, -180.0))

    private lateinit var googleApiClient: GoogleApiClient

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
        background?.setOnClickListener { dismiss() }

        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

    }

    private fun initDialog(){

    }

    override fun onStart() {
            googleApiClient.connect()
        super.onStart()
    }

    override fun onStop() {
        if (googleApiClient.isConnected){
            googleApiClient.disconnect()
        }
        super.onStop()
    }

    override fun onConnected(p0: Bundle?) {
        initDialog()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e(tag, "Google API client suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e(tag, "onConnectionFailed: ConnectionResult.getErrorCode() = " + p0.errorCode)
    }
}