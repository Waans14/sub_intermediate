package com.millenialzdev.storyapp.view.maps

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.millenialzdev.storyapp.R
import com.millenialzdev.storyapp.ViewModelFactory
import com.millenialzdev.storyapp.databinding.ActivityMapsBinding
import com.millenialzdev.storyapp.remote.response.StoryResponse
import com.millenialzdev.storyapp.remote.retrofit.ApiConfig
import com.millenialzdev.storyapp.remote.viewModel.MapsViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.getSession().observe(this@MapsActivity) { user ->

            viewModel.viewModelScope.launch {

                val client = ApiConfig.getApiService(user.token).getStoriesWithLocation()
                client.enqueue(object : Callback<StoryResponse> {
                    override fun onResponse(
                        call: Call<StoryResponse>,
                        response: Response<StoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.listStory?.forEach { story ->
                                val latLng = story.lat?.let { story.lon?.let { it1 ->
                                    LatLng(it,
                                        it1
                                    )
                                } }
                                latLng?.let {
                                    MarkerOptions()
                                        .position(it)
                                        .title(story.name)
                                        .snippet(story.description)
                                }?.let {
                                    mMap.addMarker(
                                        it
                                    )
                                }
                            }
                        } else {
                            Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    }
                })
            }
        }


    }
}