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
                   var target: ArrayList<String> = ArrayList()): Parcelable {

    fun signUp(): String{
        //Log.d("TAG", "at account" + usrName + email + password)
        if(!isValidUserName()){
            return "Invalid Username"
        }
        if(!isValidEmail()){
            return "Invalid Email"
        }
        if(!isValidPasswordFormat()){
            return "Invalid Password"
        }
        return "Success"
    }
    private fun isValidUserName(): Boolean{
        return !TextUtils.isEmpty(usrName)
    }
    private fun isValidEmail(): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPasswordFormat(): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[!@#$%^&*()])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        );
        return passwordREGEX.matcher(password).matches() && !TextUtils.isEmpty(password)
    }
}