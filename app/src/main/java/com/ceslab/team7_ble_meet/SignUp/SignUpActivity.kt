package com.ceslab.team7_ble_meet.SignUp

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ceslab.team7_ble_meet.R
import com.ceslab.team7_ble_meet.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSignUpActivity()
        binding.apply {
            btnSignUp.setOnClickListener{
                Toast.makeText(this@SignUpActivity,"Clicked",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setUpSignUpActivity(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this@SignUpActivity).get(SignUpViewModel::class.java)
    }
}