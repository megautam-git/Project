package com.example.project

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.*

import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.*



class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationManager: LocationManager? = null
    private val permissionCode = 101
    private var googleMap: GoogleMap? = null
    private var markerOptions: MarkerOptions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        setUpLocationListener()
    }

    private fun setUpLocationListener() {
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Log.d("TAG", "onLocationResult: ${location.latitude} ${location.longitude}")
                        val latLng = LatLng(location.latitude, location.longitude)
                        markerOptions = MarkerOptions().position(latLng).title("I am here!")
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                        googleMap?.clear()
                        googleMap?.addMarker(markerOptions!!)
                    }

                }
            },
            Looper.myLooper()
        )
    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener {

            if (it != null) {
                currentLocation = it
                /*  Toast.makeText(applicationContext, currentLocation.latitude.toString() + ":" +
                          currentLocation.longitude, Toast.LENGTH_SHORT).show()*/
                val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
//                markerOptions = MarkerOptions().position(latLng).title("I am here!")
//                googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
//                googleMap?.addMarker(markerOptions!!)
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.myMap) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode ->
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    fetchLocation()
                }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        p0.uiSettings.isCompassEnabled = true
        p0.uiSettings.isZoomControlsEnabled = true
        p0.uiSettings.isZoomGesturesEnabled = true
        p0.uiSettings.isIndoorLevelPickerEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        p0.isMyLocationEnabled = true
        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        markerOptions = MarkerOptions().position(latLng).title("I am here!")
        p0.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        p0.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        p0.addMarker(markerOptions!!)
        // drawMarker(currentLocation.latitude,currentLocation.longitude)
    }

    /*override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        googleMap?.addMarker(MarkerOptions().position(LatLng(lat, lng)).title("Marker"))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 17f))
        Log.i("position","Latitude $lat: $lng")
        Toast.makeText(this, "$lat : $lng", Toast.LENGTH_SHORT).show()

    }*/

    /*private fun drawMarker(lat:Double,lng:Double) {
        val marker = MarkerOptions().position(LatLng(lat, lng)).title(" Maps Tutorial")
            .snippet("Android Ruler")
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        val cameraPosition =
            CameraPosition.Builder().target(LatLng(lat, lng)).zoom(12f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.addMarker(marker)
    }
*/

}
