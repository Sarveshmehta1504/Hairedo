package com.example.hairedo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.hairedo.databinding.ActivityEnterMobileNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterMobileNumber : AppCompatActivity() {
    private lateinit var binding: ActivityEnterMobileNumberBinding
    private lateinit var number: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Hairedo)
        binding = ActivityEnterMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.phoneProgressBar.visibility = View.INVISIBLE
        val ccp = binding.ccp
        ccp.registerCarrierNumberEditText(binding.phoneNumberEt)
        binding.getOtpBtn.setOnClickListener {
            number = ccp.fullNumber.trim()
            if(number.isNotEmpty()){
                number = "+${number}"
                binding.phoneProgressBar.visibility = View.VISIBLE

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(number) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this) // Activity (for callback binding)
                    .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

                }else{
                    Toast.makeText(this,"Please Enter Number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.skipTv.setOnClickListener {
            startActivity(Intent(this@EnterMobileNumber, GoogleAuthActivity::class.java))
            finish()
        }

    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.e("TAG", "onVerificationFailed: $e" )
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.e("TAG", "onVerificationFailed: $e" )

            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.e("TAG", "onVerificationFailed: $e" )
            }
            binding.phoneProgressBar.visibility = View.VISIBLE

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            val intent = Intent(this@EnterMobileNumber, OTPActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("mobileNumber", number)
            startActivity(intent)
            binding.phoneProgressBar.visibility = View.INVISIBLE

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    }
                    // Update UI
                }
                    binding.phoneProgressBar.visibility = View.INVISIBLE

            }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun sendToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}