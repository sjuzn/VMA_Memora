package sk.upjs.druhypokus.moments.ui

//https://medium.com/@mr.appbuilder/how-to-integrate-and-work-with-open-street-map-osm-in-an-android-app-kotlin-564b38590bfe

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.GpsStatus
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import sk.upjs.druhypokus.R
import kotlin.properties.Delegates

class MapaActivity : AppCompatActivity(), MapListener, GpsStatus.Listener, MapEventsReceiver {

    private lateinit var mMap: MapView
    private lateinit var controller: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay
    private var marker: Marker? = null

    private var latitude : Float = 0.0f
    private var longitude : Float = 0.0f
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap()
        } else {
            checkLocationPermission()
        }

        findViewById<Button>(R.id.btOK).setOnClickListener{
            val previousScreen = Intent(
                applicationContext,
                AddMomentActivity::class.java
            )
            previousScreen.putExtra("latitude", latitude)
            previousScreen.putExtra("longitude", longitude)
            setResult(2, previousScreen)
            finish()
        }

    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        initializeMap()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap()
            }
        }
    }

    private fun initializeMap() {
        mMap = findViewById(R.id.osmmap)
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                val myLocation = mMyLocationOverlay.myLocation
                if (myLocation != null) {
                    controller.setCenter(myLocation)
                    controller.setZoom(15.0)
                    controller.animateTo(myLocation)
                }
            }
        }

        mMap.overlays.add(mMyLocationOverlay)
        mMap.addMapListener(this)

        val mapEventsOverlay = MapEventsOverlay(this)
        mMap.overlays.add(mapEventsOverlay)
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
       // Log.e("TAG", "onCreate:la ${event?.source?.mapCenter?.latitude}")
       // Log.e("TAG", "onCreate:lo ${event?.source?.mapCenter?.longitude}")
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
       // Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onGpsStatusChanged(event: Int) {
        // Implementation for GPS status changes
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        p?.let {
            latitude = it.latitude.toFloat()
            longitude = it.longitude.toFloat()
            controller.animateTo(it)

            // Remove the existing marker if it exists
            marker?.let { existingMarker ->
                mMap.overlays.remove(existingMarker)
            }

            // Create a new marker at the tapped location
            marker = Marker(mMap).apply {
                position = GeoPoint(latitude.toDouble(), longitude.toDouble())
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            // Add the marker to the map
            mMap.overlays.add(marker)

            // Redraw the map to show the marker
            mMap.invalidate()

            //Toast.makeText(this, "Tapped at: $latitude, $longitude", Toast.LENGTH_SHORT).show()
            Log.d("MapTapReceiver", "Tapped at: $latitude, $longitude")
        }
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return true
    }


}
