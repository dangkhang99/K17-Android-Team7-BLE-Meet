package com.ceslab.team7_ble_meet.Model

import android.os.Parcelable
import android.text.TextUtils
import android.util.Patterns
import kotlinx.android.parcel.Parcelize
import java.util.regex.Pattern

@Parcelize
data class Account(var usrName: String = "",
                   var email: String = "",
                   var password: String = "",
                   var phoneNumber: String = "",
                   var self: ArrayList<String> = ArrayList(),
                   var target: ArrayList<String> = ArrayList()): Parcelable