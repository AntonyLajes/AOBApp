package com.example.chat_app_prototype_v6.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chat_app_prototype_v6.databinding.ActivitySplashBinding
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val auth = FirebaseInstance.getAuthenticationInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            if(auth.currentUser == null){
                startActivity(Intent(this, AuthenticationActivity::class.java))
            }else{
                startActivity(Intent(this, MainActivity::class.java))
            }
        }, 1000)
    }
}