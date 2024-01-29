package com.example.hairedo

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.example.hairedo.databinding.ActivityForgetPasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgetPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.SubmitBtn.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword(){
        val email = binding.emailForgotEt.text.toString()
        if(validateForm(email)){
            showProgressBar()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    hideProgressBar()
                    binding.forgotBox.visibility = View.INVISIBLE
                    binding.successTv.visibility = View.VISIBLE
                }
                else{
                    hideProgressBar()
                    showToast(this, "Can't not reset your password. Try again after sometime")
                }
            }
        }

    }

    private fun validateForm(email: String): Boolean {
        return when{
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailForgotEt.error = "Enter valid email address"
                false
            }
            else -> true
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}