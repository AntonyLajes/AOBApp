package com.example.chat_app_prototype_v6.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chat_app_prototype_v6.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}