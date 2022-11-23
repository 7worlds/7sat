package fhnw.ws6c.sevensat.data.gps

import com.google.android.gms.location.LocationServices
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class UserLocationSubject(
  private val activity: Activity,
  private val minLocationUpdateFrequencyMs: Long  = 10000,
  private val maxLocationUpdateFrequencyMs: Long  = 5000L,
  private val locationUpdatePriority      : Int   = LocationRequest.PRIORITY_HIGH_ACCURACY,
  onPermissionDenied:   () -> Unit
) {
  private val locationCallback        : LocationCallback
  private val onNewLocationCallbacks  = mutableMapOf<String, (location: Location)  -> Unit>()
  private val locationProvider by lazy { LocationServices.getFusedLocationProviderClient(activity) }

  private val permissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )

  init {
    locationCallback = createLocationCallback()
    ActivityCompat.requestPermissions(activity, permissions, 10)
    initializeLocationListener(onPermissionDenied)
  }

  /**
   * @param onNewLocation gets called as soon as a new location is provided.
   */
  fun addLocationObserver(key: String, onNewLocation: (location: Location)  -> Unit){
    onNewLocationCallbacks[key] = (onNewLocation)
  }

  /**
   * @param onNewLocation do not call this function on location changes anymore.
   */
  fun removeLocationObserver(key: String) =
    onNewLocationCallbacks.remove(key)

  /**
   * Cancel location updates at all.
   */
  fun cancelLocationListener() = locationProvider.removeLocationUpdates(locationCallback)

  private fun initializeLocationListener(onPermissionDenied: () -> Unit)  {
    if (permissions.oneOfGranted()) listenToNewLocations()
    else onPermissionDenied()
  }

  @SuppressLint("MissingPermission")
  private fun listenToNewLocations() {
    locationProvider.requestLocationUpdates(
      locationRequest(),
      locationCallback,
      Looper.getMainLooper()
    )
  }

  /**
   * Defines a repetitive notification request
   */
  private fun locationRequest() = LocationRequest.create().apply {
    interval = minLocationUpdateFrequencyMs
    fastestInterval = maxLocationUpdateFrequencyMs
    priority = locationUpdatePriority
  }

  private fun  createLocationCallback() = object : LocationCallback() {
    override fun onLocationResult(p0: LocationResult) {
      val currentLocation = p0.locations.last()
      onNewLocationCallbacks.forEach { it.value(currentLocation) }
    }
  }

  private fun Array<String>.oneOfGranted(): Boolean = any { it.granted() }
  private fun String.granted(): Boolean =
    ActivityCompat.checkSelfPermission(activity, this) == PackageManager.PERMISSION_GRANTED
}

