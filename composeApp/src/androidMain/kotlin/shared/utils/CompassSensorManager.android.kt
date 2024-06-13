package shared.utils
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class CompassSensorManager(private val context: Context) : SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0

    actual fun azimuth():Flow<Float>{
        return mazimuth
    }

    var mazimuth: MutableStateFlow<Float> = MutableStateFlow(0f)


    actual fun qiblaDirection():Flow<Double>{
        return mqiblaDirection
    }

    var mqiblaDirection: MutableStateFlow<Double> = MutableStateFlow(0.0)


    actual fun start() {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        startLocationUpdates()
    }

    actual fun stop() {
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, magnetometer)
        stopLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                currentLatitude = it.latitude
                currentLongitude = it.longitude
                mqiblaDirection.value = QiblaUtils.calculateQiblaDirection(currentLatitude, currentLongitude)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravity, 0, event.values.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
        }

        SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
        SensorManager.getOrientation(rotationMatrix, orientation)

        mazimuth.value = Math.toDegrees(orientation[0].toDouble()).toFloat()
        mazimuth.value = (mazimuth.value + 360) % 360
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        val accuracyMessage = when (accuracy) {
            SensorManager.SENSOR_STATUS_UNRELIABLE -> "Unreliable"
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "Low accuracy"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium accuracy"
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High accuracy"
            else -> "Unknown accuracy"
        }
        Toast.makeText(context, "Sensor accuracy changed: $accuracyMessage", Toast.LENGTH_SHORT).show()
    }
}