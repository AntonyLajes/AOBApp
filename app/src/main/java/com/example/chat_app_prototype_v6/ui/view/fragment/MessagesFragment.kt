package com.example.chat_app_prototype_v6.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app_prototype_v6.databinding.FragmentMessagesBinding
import com.example.chat_app_prototype_v6.ui.view.adapter.MessageAdapter
import com.example.chat_app_prototype_v6.util.datamodel.LastMessageModel

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding: FragmentMessagesBinding get() = _binding!!
    private lateinit var messageAdapter: MessageAdapter
    private var lastMessageList: ArrayList<LastMessageModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastMessageAdapterHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun lastMessageAdapterHandler(){
        messageListMocked()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessageAdapter(requireContext())
        binding.recyclerView.adapter = messageAdapter
        messageAdapter.getLastMessageList(lastMessageList)
    }

    private fun messageListMocked(){
        for(x in 0..10){
            lastMessageList.add(LastMessageModel(
                "Alana Dias",
                "https://firebasestorage.googleapis.com/v0/b/chat-app-prototype-v6.appspot.com/o/profilePictures%2F%5BB%40118e411.jpeg?alt=media&token=009ca449-070b-4cc6-9645-9bacf65f1039",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                "11:09"
            ))
        }
    }
}