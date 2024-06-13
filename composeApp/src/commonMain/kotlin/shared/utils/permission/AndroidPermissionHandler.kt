package shared.utils.permission

expect class AndroidPermissionCheck {

    fun isLocationPermissionGranted(): Boolean
    fun openAppSettings()
}