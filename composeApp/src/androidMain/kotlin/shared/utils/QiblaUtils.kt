package shared.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object QiblaUtils {
    private const val KAABA_LATITUDE = 21.4225
    private const val KAABA_LONGITUDE = 39.8262


    fun calculateQiblaDirection(latitude: Double, longitude: Double): Double {
        val lat1 = Math.toRadians(latitude)
        val lon1 = Math.toRadians(longitude)
        val lat2 = Math.toRadians(KAABA_LATITUDE)
        val lon2 = Math.toRadians(KAABA_LONGITUDE)

        val deltaLon = lon2 - lon1
        val x = sin(deltaLon) * cos(lat2)
        val y = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLon)
        val initialBearing = atan2(x, y)

        return (Math.toDegrees(initialBearing) + 360) % 360.toFloat()
    }
}