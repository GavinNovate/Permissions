package net.novate.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.util.SparseArray
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

@Suppress("MemberVisibilityCanBePrivate")
object Permissions {
    const val PERMISSION_GRANTED = 0
    const val PERMISSION_DENIED = -1
    const val PERMISSION_IGNORE = -2
    const val PERMISSION_CANCEL = -3

    private val requests by lazy { SparseArray<Pair<Array<String>, (Array<String>, IntArray) -> Unit>>() }

    @MainThread
    fun requestPermissions(
        activity: FragmentActivity,
        permissions: Array<String>,
        callback: (permissions: Array<String>, results: IntArray) -> Unit
    ) {
        requestPermissions(activity.lifecycle, activity, permissions, callback)
    }

    @MainThread
    fun requestPermissions(
        fragment: Fragment,
        permissions: Array<String>,
        callback: (permissions: Array<String>, results: IntArray) -> Unit
    ) {
        fragment.activity?.let { requestPermissions(fragment.lifecycle, it, permissions, callback) }
    }

    private fun requestPermissions(
        lifecycle: Lifecycle,
        activity: Activity,
        permissions: Array<String>,
        callback: (permissions: Array<String>, results: IntArray) -> Unit
    ) {
        lifecycle.addObserver(object : LifecycleObserver {
            private val code = code().also { requests[it] = permissions to callback }

            init {
                ActivityCompat.requestPermissions(activity, permissions, code)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                requests[code]?.also { requests.remove(code) }?.let { (permissions, callback) ->
                    callback(permissions, IntArray(permissions.size) { PERMISSION_CANCEL })
                }
            }
        })
    }

    @MainThread
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        requests[requestCode]?.also { requests.remove(requestCode) }?.let { (permissions, callback) ->
            callback(permissions, IntArray(permissions.size) {
                if (it < grantResults.size) {
                    if (grantResults[it] == PackageManager.PERMISSION_GRANTED) {
                        PERMISSION_GRANTED
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[it])) {
                            PERMISSION_DENIED
                        } else {
                            PERMISSION_IGNORE
                        }
                    }
                } else {
                    PERMISSION_CANCEL
                }
            })
        }
    }

    private fun code(): Int =
        Iterable { requests.keyIterator().withIndex() }.firstOrNull { it.index != it.value }?.index ?: requests.size()
}

@MainThread
fun FragmentActivity.requestPermissions(
    permissions: Array<String>,
    callback: (permissions: Array<String>, results: IntArray) -> Unit
) {
    Permissions.requestPermissions(this, permissions, callback)
}

@MainThread
fun Fragment.requestPermissions(
    permissions: Array<String>,
    callback: (permissions: Array<String>, results: IntArray) -> Unit
) {
    Permissions.requestPermissions(this, permissions, callback)
}

@MainThread
fun FragmentActivity.onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
    Permissions.onRequestPermissionsResult(this, requestCode, grantResults)
}