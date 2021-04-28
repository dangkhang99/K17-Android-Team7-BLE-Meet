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
                   var self_level_1: ArrayList<Int> = ArrayList(),
                   var self_level_2: ArrayList<Int> = ArrayList(),
                   var target_level_1: ArrayList<Int> = ArrayList(),
                   var target_level_2: ArrayList<Int> = ArrayList()): Parcelable