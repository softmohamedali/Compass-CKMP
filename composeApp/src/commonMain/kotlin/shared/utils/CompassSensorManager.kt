package shared.utils

import kotlinx.coroutines.flow.Flow

expect class CompassSensorManager {
    fun azimuth(): Flow<Float>
    fun qiblaDirection(): Flow<Double>
    fun start()
    fun stop()

}