package net.novate.permissions

interface OnRequestPermissionsResultListener {
    fun onResult(permissions: Array<out String>, results: IntArray)
}