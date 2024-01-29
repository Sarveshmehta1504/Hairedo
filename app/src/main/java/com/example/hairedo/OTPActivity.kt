package com.example.hairedo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.hairedo.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var OTP: String
    private lateinit var mobileNumber: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.phoneProgressBar.visibility = View.INVISIBLE
        binding.textMobile.setText(String.format("%s", intent.getStringExtra("mobileNumber")))
        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        mobileNumber = intent.getStringExtra("mobileNumber")!!
        resendOTPVisibility()
        binding.Continue.setOnClickListener{
            val typedOTP = binding.inputOtpBox.text.toString()
            if(typedOTP.isNotEmpty()){
                if(typedOTP.length == 6){
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    binding.phoneProgressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this, "Please Enter correct OTP", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.resendOTP.setOnClickListener {
            resendVerificationCode()
            resendOTPVisibility()
        }

        binding.backArrow.setOnClickListener{
            startActivity(Intent(this, EnterMobileNumber::class.java))
        }

    }
    private fun resendOTPVisibility(){
        binding.inputOtpBox.setText("")
        binding.retryVia.visibility = View.INVISIBLE
        binding.resendOTP.visibility = View.INVISIBLE
        binding.resendOTP.isEnabled = false
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            binding.retryVia.visibility = View.VISIBLE
            binding.resendOTP.visibility = View.VISIBLE
            binding.resendOTP.isEnabled = true
        }, 60000)

    }
    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.phoneProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    binding.phoneProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "The verification code ${task.exception.toString()}",Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                binding.phoneProgressBar.visibility = View.VISIBLE
            }
    }
    private fun sendToMain(){
        startActivity(Intent(this, MainActivity::class.java))
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
                Log.e("TAG", "onVerificationFailed: ${e.toString()}" )
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.e("TAG", "onVerificationFailed: ${e.toString()}" )

            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.e("TAG", "onVerificationFailed: ${e.toString()}" )

            }
            binding.phoneProgressBar.visibility = View.VISIBLE
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token
        }
    }

}