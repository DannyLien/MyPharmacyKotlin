package com.hom.pharmacy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.hom.pharmacy.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String? = MapsActivity::class.java.simpleName
    private var filterData: Feature? = null
    private var pharmInfo: PharmacyInfo? = null
    private var myMarker: Marker? = null
    private val requestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            it[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            setUpdataLocation()
            myLocation()
        } else {
            Snackbar.make(binding.root, "Location Permission Denied", Snackbar.LENGTH_LONG).show()
        }
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        pharmInfo = PharmacyViewModel.PharmInfoData.pharmInfoGson
        filterData = PharmacyViewModel.PharmInfoData.pharmFilterData
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpdataLocation()
        myLocation()
        mMap.setInfoWindowAdapter(MyInfoAdapter(this))
        mMap.setOnInfoWindowClickListener {
            val clkName = it.title
            pharmInfo?.also { pharm ->
                val data = pharm.features.filter { it.properties.name == clkName }
                PharmacyViewModel.PharmInfoData.pharmFilterData = data.get(0)
//                Log.d(TAG, "onMapReady: mask-clkMarker- ${data.get(0)}")
                Intent(this, PharmacyActivity::class.java)
                    .also { startActivity(it) }
            }
        }
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun setUpdataLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener {
            if (it != null && filterData == null) {
                val latlng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f))
                myMarker?.remove()
                myMarker = mMap.addMarker(MarkerOptions().position(latlng).title("Now Location"))
                setAllMarker()
            } else {
                filterData?.also {
                    val lat = it.geometry.coordinates.get(1)
                    val lng = it.geometry.coordinates.get(0)
                    val latlng = LatLng(lat, lng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19f))
                    myMarker?.remove()
                    myMarker =
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latlng)
                                .title(it.properties.name)
                                .snippet("成人:${it.properties.mask_adult},兒童:${it.properties.mask_child}")
                        )
                }
            }
        }
    }

    private fun myLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMyLocationButtonClickListener {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                if (it != null) {
                    val latlng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f))
                    myMarker?.remove()
                    myMarker =
                        mMap.addMarker(MarkerOptions().position(latlng).title("Now Location"))
                }
            }
            true
        }
    }

    private fun setAllMarker() {
        pharmInfo?.also { pharm ->
            pharm.features.forEach {
                val latLng = LatLng(it.geometry.coordinates.get(1), it.geometry.coordinates.get(0))
                myMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.properties.name)
                        .snippet("成人:${it.properties.mask_adult},兒童:${it.properties.mask_child}")
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pharm_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = item.itemId
        when (menuId) {
            R.id.action_back -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}





