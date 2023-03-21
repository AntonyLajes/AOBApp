package com.example.chat_app_prototype_v6.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLetsStart.setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_insertNumberFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}