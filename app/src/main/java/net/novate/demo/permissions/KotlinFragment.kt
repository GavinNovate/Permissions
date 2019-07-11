package net.novate.demo.permissions


import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.novate.permissions.Permissions
import net.novate.permissions.hasPermissions
import net.novate.permissions.requestAllManifestPermissions
import net.novate.permissions.requestPermissions

class KotlinFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kotlin, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "hasPermissions: READ_EXTERNAL_STORAGE & WRITE_EXTERNAL_STORAGE")
        }

        requestPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) { permissions, results ->
            if (results.all { it == Permissions.GRANTED }) {
                Log.d(TAG, "requestPermissions: Has all permissions: $permissions")
            }
            Log.d(TAG, "GRANTED:${permissions.filterIndexed { index, _ -> results[index] == Permissions.GRANTED }}")
            Log.d(TAG, "DENIED:${permissions.filterIndexed { index, _ -> results[index] == Permissions.DENIED }}")
            Log.d(TAG, "IGNORE:${permissions.filterIndexed { index, _ -> results[index] == Permissions.IGNORE }}")
            Log.d(TAG, "CANCEL:${permissions.filterIndexed { index, _ -> results[index] == Permissions.CANCEL }}")
        }

        requestAllManifestPermissions { permissions, results ->
            if (results.all { it == Permissions.GRANTED }) {
                Log.d(TAG, "requestPermissions: Has all permissions: $permissions")
            }
            Log.d(TAG, "GRANTED:${permissions.filterIndexed { index, _ -> results[index] == Permissions.GRANTED }}")
            Log.d(TAG, "DENIED:${permissions.filterIndexed { index, _ -> results[index] == Permissions.DENIED }}")
            Log.d(TAG, "IGNORE:${permissions.filterIndexed { index, _ -> results[index] == Permissions.IGNORE }}")
            Log.d(TAG, "CANCEL:${permissions.filterIndexed { index, _ -> results[index] == Permissions.CANCEL }}")
        }
    }

    companion object {
        const val TAG = "KotlinFragment"
    }
}