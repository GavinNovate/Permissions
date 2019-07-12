package net.novate.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.support.annotation.MainThread
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun Context.hasPermissions(vararg permissions: String) =
    permissions.all { ActivityCompat.checkSelfPermission(this, it) == Permissions.GRANTED }

fun Fragment.hasPermissions(vararg permissions: String) =
    permissions.all { ActivityCompat.checkSelfPermission(this.requireContext(), it) == Permissions.GRANTED }

@MainThread
fun FragmentActivity.requestPermissions(
    vararg permissions: String,
    callback: (permissions: Array<out String>, results: IntArray) -> Unit
) {
    Permissions.requestPermissions(lifecycle, this, permissions, callback)
}

@MainThread
fun Fragment.requestPermissions(
    vararg permissions: String,
    callback: (permissions: Array<out String>, results: IntArray) -> Unit
) {
    Permissions.requestPermissions(lifecycle, requireActivity(), permissions, callback)
}

@MainThread
fun FragmentActivity.requestAllManifestPermissions(callback: (permissions: Array<out String>, results: IntArray) -> Unit) {
    Permissions.requestPermissions(lifecycle, this, allManifestPermissions(), callback)
}

@MainThread
fun Fragment.requestAllManifestPermissions(callback: (permissions: Array<out String>, results: IntArray) -> Unit) {
    Permissions.requestPermissions(lifecycle, requireActivity(), requireContext().allManifestPermissions(), callback)
}

@MainThread
fun FragmentActivity.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
    Permissions.onRequestPermissionsResult(this, requestCode, grantResults)
}

fun Context.allManifestPermissions(): Array<String> =
    this.packageManager.getPackageInfo(
        this.packageName,
        PackageManager.GET_PERMISSIONS
    ).requestedPermissions ?: emptyArray()