package di

import org.koin.dsl.module
import shared.utils.CompassSensorManager

actual fun platformModule() =
    module {

        single { CompassSensorManager(get()) }

    }
