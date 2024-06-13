package shared.utils

import kotlinx.coroutines.flow.Flow

actual class CompassSensorManager {
    actual fun azimuth(): Flow<Float> {
        TODO("Not yet implemented")
    }

    actual fun qiblaDirection(): Flow<Double> {
        TODO("Not yet implemented")
    }

    actual fun start() {
    }

    actual fun stop() {
    }

}