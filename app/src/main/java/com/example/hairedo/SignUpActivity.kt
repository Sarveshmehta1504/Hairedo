package com.example.hairedo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.example.hairedo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding: ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.agreeAndContinueBtn?.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        val name = binding?.nameEt?.text.toString()
        val email = binding?.emailSignUpEt?.text.toString()
        val password = binding?.passwordEt?.text.toString()
        val confirmPassword = binding?.confirmPasswordEt?.text.toString()
        if(validateForm(name, email, password, confirmPassword)){
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        showToast(this, "User Id is created successfully")
                        hideProgressBar()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }else {
                        showToast(this, "User Id is not created.Try again later")
                        hideProgressBar()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return when{
            TextUtils.isEmpty(name) -> {
                binding?.nameEt?.error = "Enter name"
                false
            }
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()-> {
                binding?.emailSignUpEt?.error = "Enter valid email"
                false
            }
            TextUtils.isEmpty(password) -> {
                binding?.passwordEt?.error = "Enter password"
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                binding?.confirmPasswordEt?.error ="Enter confirm password"
                false
            }
            else -> { true }
        }
    }
}