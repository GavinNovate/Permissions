package net.novate.permissions

import android.app.Activity
import android.content.Context
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

@Suppress("MemberVisibilityCanBePrivate", "unused")
object Permissions {

    const val GRANTED = PackageManager.PERMISSION_GRANTED
    const val DENIED = PackageManager.PERMISSION_DENIED
    const val IGNORE = -2
    const val CANCEL = -3

    private val requests by lazy { SparseArray<Pair<Array<out String>, (Array<out String>, IntArray) -> Unit>>() }

    @JvmStatic
    fun hasPermissions(context: Context, vararg permissions: String) =
        permissions.all { ActivityCompat.checkSelfPermission(context, it) == GRANTED }

    @JvmStatic
    fun hasPermissions(fragment: Fragment, vararg permissions: String) =
        permissions.all { ActivityCompat.checkSelfPermission(fragment.requireContext(), it) == GRANTED }

    @JvmStatic
    @MainThread
    fun requestPermissions(
        activity: FragmentActivity,
        vararg permissions: String,
        listener: OnRequestPermissionsResultListener
    ) {
        requestPermissions(activity.lifecycle, activity, permissions, listener::onResult)
    }

    @JvmStatic
    @MainThread
    fun requestPermissions(
        fragment: Fragment,
        vararg permissions: String,
        listener: OnRequestPermissionsResultListener
    ) {
        requestPermissions(
            fragment.lifecycle,
            fragment.requireActivity(),
            permissions,
            listener::onResult
        )
    }

    @JvmStatic
    @MainThread
    fun requestAllManifestPermissions(
        activity: FragmentActivity,
        listener: OnRequestPermissionsResultListener
    ) {
        requestPermissions(activity.lifecycle, activity, activity.allManifestPermissions(), listener::onResult)
    }

    @JvmStatic
    @MainThread
    fun requestAllManifestPermissions(
        fragment: Fragment,
        listener: OnRequestPermissionsResultListener
    ) {
        requestPermissions(
            fragment.lifecycle,
            fragment.requireActivity(),
            fragment.requireContext().allManifestPermissions(),
            listener::onResult
        )
    }

    internal fun requestPermissions(
        lifecycle: Lifecycle,
        activity: Activity,
        permissions: Array<out String>,
        callback: (permissions: Array<out String>, results: IntArray) -> Unit
    ) {
        lifecycle.addObserver(object : LifecycleObserver {
            private val code = code().also { requests[it] = permissions to callback }

            init {
                ActivityCompat.requestPermissions(activity, permissions, code)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                requests[code]?.also { requests.remove(code) }?.let { (permissions, callback) ->
                    callback(permissions, IntArray(permissions.size) { CANCEL })
                }
            }
        })
    }

    @JvmStatic
    @MainThread
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        requests[requestCode]?.also { requests.remove(requestCode) }?.let { (permissions, callback) ->
            callback(permissions, IntArray(permissions.size) {
                if (it < grantResults.size) {
                    if (grantResults[it] == PackageManager.PERMISSION_GRANTED) {
                        GRANTED
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[it])) {
                            DENIED
                        } else {
                            IGNORE
                        }
                    }
                } else {
                    CANCEL
                }
            })
        }
    }

    private fun code(): Int =
        Iterable { requests.keyIterator().withIndex() }.firstOrNull { it.index != it.value }?.index ?: requests.size()
}