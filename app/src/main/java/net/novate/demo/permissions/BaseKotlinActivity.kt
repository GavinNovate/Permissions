package net.novate.demo.permissions

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import net.novate.permissions.onRequestPermissionsResult

@SuppressLint("Registered")
open class BaseKotlinActivity : AppCompatActivity() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        onRequestPermissionsResult(requestCode, grantResults)
    }
}