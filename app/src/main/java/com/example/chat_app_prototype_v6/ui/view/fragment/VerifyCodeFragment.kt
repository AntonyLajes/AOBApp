package com.example.chat_app_prototype_v6.ui.view.fragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.ActivityMainBinding
import com.example.chat_app_prototype_v6.databinding.FragmentVerifyCodeBinding
import com.example.chat_app_prototype_v6.ui.view.activity.CreateProfileActivity
import com.example.chat_app_prototype_v6.ui.view.activity.MainActivity
import com.example.chat_app_prototype_v6.ui.viewmodel.VerifyCodeViewModel

class VerifyCodeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentVerifyCodeBinding? = null
    private val binding: FragmentVerifyCodeBinding get() = _binding!!
    private lateinit var viewmodel: VerifyCodeViewModel
    private val navigationArgs: VerifyCodeFragmentArgs by navArgs()
    private var verificationCode: String = ""
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val firstVerifyNumber = binding.firstVerifyNumber
            val secondVerifyNumber = binding.secondVerifyNumber
            val thirdVerifyNumber = binding.thirdVerifyNumber
            val fourthVerifyNumber = binding.fourthVerifyNumber
            val fifthVerifyNumber = binding.fifthVerifyNumber
            val sixthVerifyNumber = binding.sixthVerifyNumber

            verificationCode =
                firstVerifyNumber.text.toString() + secondVerifyNumber.text.toString() + thirdVerifyNumber.text.toString() + fourthVerifyNumber.text.toString() + fifthVerifyNumber.text.toString() + sixthVerifyNumber.text.toString()

            if (firstVerifyNumber.text.isNotEmpty()) {
                secondVerifyNumber.showKeyboard()
                if (secondVerifyNumber.text.isNotEmpty()) {
                    thirdVerifyNumber.showKeyboard()
                    if (thirdVerifyNumber.text.isNotEmpty()) {
                        fourthVerifyNumber.showKeyboard()
                        if (fourthVerifyNumber.text.isNotEmpty()) {
                            fifthVerifyNumber.showKeyboard()
                            if (fifthVerifyNumber.text.isNotEmpty()) {
                                sixthVerifyNumber.showKeyboard()
                                if (sixthVerifyNumber.text.isNotEmpty()) {
                                    sixthVerifyNumber.hideKeyboard()
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyCodeBinding.inflate(inflater)
        viewmodel = ViewModelProvider(this).get(VerifyCodeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        binding.codeVerifySubtitle.text = binding.codeVerifySubtitle.text.toString() + navigationArgs.phoneNumber
    }

    override fun onResume() {
        super.onResume()
        observers()
        viewmodel.sendMessageCode(navigationArgs.phoneNumber, requireActivity())
        Handler().postDelayed({
            inputCodeHandler()
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.buttonVerifyCode.id -> {
                Toast.makeText(requireContext(), verificationCode, Toast.LENGTH_SHORT).show()
                viewmodel.verifyAuthenticationCode(verificationCode)
            }
        }
    }

    private fun initClicks() {
        binding.buttonVerifyCode.setOnClickListener(this)
    }

    private fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun View.hideKeyboard() {
        this.clearFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun observers() {
        viewmodel.authStatus.observe(viewLifecycleOwner) { verification ->
            if (verification.status) {
                requireActivity().finish()
                startActivity(Intent(requireContext(), CreateProfileActivity::class.java))
            } else {
                makeToast(verification.message)
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun inputCodeHandler() {
        binding.firstVerifyNumber.showKeyboard()
        binding.firstVerifyNumber.addTextChangedListener(textWatcher)
        binding.secondVerifyNumber.addTextChangedListener(textWatcher)
        binding.thirdVerifyNumber.addTextChangedListener(textWatcher)
        binding.fourthVerifyNumber.addTextChangedListener(textWatcher)
        binding.fifthVerifyNumber.addTextChangedListener(textWatcher)
        binding.sixthVerifyNumber.addTextChangedListener(textWatcher)
    }
}