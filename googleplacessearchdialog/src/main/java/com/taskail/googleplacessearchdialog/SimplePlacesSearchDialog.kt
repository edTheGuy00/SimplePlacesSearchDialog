package com.taskail.googleplacessearchdialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Place
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

    private val THRESH_HOLD = 1

    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerFrame: FrameLayout
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var mHandler: Handler

    interface LocationSelectedCallback{
        fun onLocationSelected(place: Place)
    }

    override fun onPlaceSelected(place: Place) {
        builder.locationSelectedListener?.onLocationSelected(place)

        hideKeyboard()
        this.dismiss()
    }

    override fun onError() {
        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
    }


    init {
        setContentView(R.layout.dialog_simple_search)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        if (builder.latLngBounds != null) {
            BOUNDS_WORLD = builder.latLngBounds!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val background : View? = findViewById(R.id.touchable_background)
        showKeyboard()

        recyclerView = findViewById(R.id.recyclerView)!!
        recyclerFrame = findViewById(R.id.recyclerFrame)!!
        loadingIndicator = findViewById(R.id.loadingIndicator)!!
        mHandler = Handler()

        background?.setOnClickListener {
            dismiss()
            hideKeyboard()
        }

        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

    }

    override fun onLoading(showLoading: Boolean) {

        mHandler.post {
            if(showLoading){
                loadingIndicator.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else{
                loadingIndicator.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

    }

    private fun initDialog(){

        val adapter = PlaceAutocompleteAdapter(mContext,
                googleApiClient,
                BOUNDS_WORLD,
                this,
                builder.searchFilter)

        recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = adapter

        getInputET().addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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

    private fun showKeyboard(){
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun hideKeyboard(){
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
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