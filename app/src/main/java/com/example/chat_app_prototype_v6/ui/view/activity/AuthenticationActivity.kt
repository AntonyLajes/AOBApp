package com.example.chat_app_prototype_v6.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat_app_prototype_v6.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}