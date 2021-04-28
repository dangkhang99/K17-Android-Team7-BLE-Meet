package com.ceslab.team7_ble_meet

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ceslab.team7_ble_meet.ble.BleHandle
import com.ceslab.team7_ble_meet.databinding.ActivityMainBinding
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    companion object {
        const val PERMISSIONS_REQUEST_CODE: Int = 12
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var data: String
    private lateinit var connect: BleHandle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        connect = BleHandle()
        checkPermissions()
        binding.apply {
            btnStartAdver.setOnClickListener {
                Log.d(TAG,"Button advertise clicked")
                data = edtData.text.toString().trim()
                connect.advertise()
            }
            btnStartDiscover.setOnClickListener {
                Log.d(TAG,"Button discover clicked")
                connect.discover()
            }
        }
    }

    private fun checkPermissions() {
        val reqPermissions = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            reqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            reqPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (reqPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, reqPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty())) {
                    grantResults.forEach { result ->
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            finish()
                            return
                        }
                    }
                }
            }
        }
    }

}