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
import com.ceslab.team7_ble_meet.databinding.ActivityMainBinding
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


class MainActivity : AppCompatActivity() {
    private val HEX_CHARS: CharArray = "0123456789ABCDEF".toCharArray()
    private val TAG: String = "MainActivity"
    companion object {
        const val PERMISSIONS_REQUEST_CODE: Int = 12
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var data: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkPermissions()
        binding.apply {
            btnStartAdver.setOnClickListener {
                Log.d(TAG,"Button advertise clicked")
                data = edtData.text.toString().trim()
                advertise()
            }
            btnStartDiscover.setOnClickListener {
                Log.d(TAG,"Button discover clicked")
                discover()
            }
        }
    }

    private fun advertise() {
        val advertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
        val settings = AdvertiseSettings.Builder()
                                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                                        .setTimeout(0)
                                        .setConnectable(false)
                                        .build()
        val pUuid = ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)))
        val data: AdvertiseData = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            AdvertiseData.Builder()
                         .setIncludeDeviceName(false)
//                         .addServiceUuid(pUuid)
                         .addServiceData(pUuid, byteArrayOf(0x01,0x02,0x03,0x04))
                         .build()
        } else {
            AdvertiseData.Builder()
                         .setIncludeDeviceName(false)
//                         .addServiceUuid(pUuid)
                         .addServiceData(pUuid, byteArrayOf(0x01,0x02,0x03,0x04))
                         .build()
        }

        val advertisingCallback: AdvertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.d(TAG, "Advertise Successfully")
                Log.d(TAG, data.toString())
                super.onStartSuccess(settingsInEffect)
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e(TAG, "Advertise Failed $errorCode")
                super.onStartFailure(errorCode)
            }
        }
        settings?.let{
            Log.d(TAG, "setting is not null")
        }
        Log.d(TAG, "data: $data")
        Log.d(TAG, "advertise: $advertisingCallback")
        advertiser.startAdvertising(settings, data, advertisingCallback)
    }

    private fun discover() {
        val mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner;
        val mScanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                Log.d(TAG, "Scan result:" + bytesToHexWhitespaceDelimited(result.scanRecord?.bytes))

//                if (result.device == null || TextUtils.isEmpty(result.device.name)) return
//
//                val builder: StringBuilder = StringBuilder(result.device.name)
//                builder.append("\n").append(
//                    result.scanRecord?.getServiceData(result.scanRecord!!.serviceUuids[0])?.let {
//                        String(it, Charset.forName("UTF-8"))
//                    }
//                )
//                Log.d(TAG, "Build on scan result: $builder")
//                binding.apply {
//                    tvDataDiscover.text = builder.toString()
//                }
            }

            override fun onBatchScanResults(results: List<ScanResult?>?) {
                super.onBatchScanResults(results)
                Log.d(TAG, "Discover successfully")
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e(TAG, "Discovery onScanFailed: $errorCode")
                super.onScanFailed(errorCode)
            }
        }

//        val filter = ScanFilter.Builder().setServiceUuid(ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)))).build()
        val filter = ScanFilter.Builder().setServiceUuid(ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)))).build()
//        val filter = ScanFilter.Builder().build()
        Log.d(TAG, "filter: ${filter.serviceUuid}")
        val filters = listOf(filter)
        val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
    }

    // Gets value in hexadecimal system
    fun bytesToHexWhitespaceDelimited(value: ByteArray?): String {
        if (value == null) {
            return ""
        }
        val hexChars = CharArray(value.size * 3)
        var v: Int
        for (j in value.indices) {
            v = value[j].toInt() and 0xFF
            hexChars[j * 3] = HEX_CHARS[v ushr 4]
            hexChars[j * 3 + 1] = HEX_CHARS[v and 0x0F]
            hexChars[j * 3 + 2] = ' '
        }
        return String(hexChars)
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