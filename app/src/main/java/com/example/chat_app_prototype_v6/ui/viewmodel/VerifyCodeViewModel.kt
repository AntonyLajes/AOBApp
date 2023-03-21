package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.StatusModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyCodeViewModel(application: Application): AndroidViewModel(application) {

    private val firebaseAuth = FirebaseInstance.getAuthenticationInstance()
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var _authStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val authStatus: LiveData<StatusModel> get() = _authStatus
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun sendMessageCode(phoneNumber: String, activity: Activity){
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyAuthenticationCode(code: String){
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                _authStatus.value = StatusModel(message = "Sucesso.")
            }else{
                _authStatus.value = StatusModel(false, "Erro.")
            }
        }
    }
}