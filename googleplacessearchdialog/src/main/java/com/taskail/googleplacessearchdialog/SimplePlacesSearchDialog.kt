package com.taskail.googleplacessearchdialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 *Created by ed on 1/21/18.
 */
class SimplePlacesSearchDialog(private val mContext: Context,
                               private var builder: PlacesSearchDialogBuilder) :
        AppCompatDialog(mContext), GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, PlaceCallback {


    private val tag = "Places Search Dialog"
    // Bounds of the world
    private var BOUNDS_WORLD = LatLngBounds(LatLng(-85.0, 180.0), LatLng(85.0, -180.0))

    private var THRESH_HOLD = 2

    private lateinit var googleApiClient: GoogleApiClient

    private var recyclerView: RecyclerView? = null

    interface LocationSelectedCallback{
        fun onLocationSelected(locationName: String,
                               locationLat: Double,
                               locationLng: Double)
    }

    override fun onPlaceSelected(placeName: String, latLng: LatLng) {
        val lat = latLng.latitude
        val lng = latLng.longitude
        builder.locationSelectedListener?.onLocationSelected(placeName, lat, lng)
    }

    override fun onError() {

    }


    init {
        setContentView(R.layout.dialog_simple_search)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        recyclerView = findViewById(R.id.recyclerView)

        if (builder.latLngBounds != null) {
            BOUNDS_WORLD = builder.latLngBounds!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        val background : View? = findViewById(R.id.touchable_background)
        background?.setOnClickListener {
            dismiss()
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

    }

    private fun initDialog(){

        val adapter = PlaceAutocompleteAdapter(mContext,
                googleApiClient,
                BOUNDS_WORLD,
                this,
                builder.searchFilter)

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView?.adapter = adapter

        getInputET().addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0!!.length > THRESH_HOLD)
                adapter.filter.filter(p0)
            }

        })

    }

    private fun getInputET() : AppCompatEditText{
        return findViewById(R.id.search_edit_text)!!
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