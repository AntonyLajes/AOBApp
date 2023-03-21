package com.example.chat_app_prototype_v6.ui.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chat_app_prototype_v6.databinding.FragmentInsertNumberBinding

class InsertNumberFragment : Fragment() {

    private var _binding: FragmentInsertNumberBinding? = null
    private val binding: FragmentInsertNumberBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertNumberBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSendCode.setOnClickListener{
            if(binding.inputPhoneNumber.isDone){
                val phoneNumber = "+55" + binding.inputPhoneNumber.unMasked
                findNavController().navigate(InsertNumberFragmentDirections.actionInsertNumberFragmentToVerifyCodeFragment(phoneNumber))
            }else{
                Toast.makeText(context, "Insira seu número de celular corretamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            binding.inputPhoneNumber.showKeyboard()
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.showKeyboard(){
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}