package com.example.chat_app_prototype_v6.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.FragmentChatBinding
import com.example.chat_app_prototype_v6.databinding.FragmentCreateProfileBinding
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.ui.view.adapter.ChatMessagesAdapter
import com.example.chat_app_prototype_v6.ui.viewmodel.ChatViewModel
import com.example.chat_app_prototype_v6.util.datamodel.MessageModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = _binding!!
    private val navigationArguments: ChatFragmentArgs by navArgs()
    private val firebaseAuthentication = FirebaseInstance.getAuthenticationInstance()
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatMessagesAdapter: ChatMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        contactData()
        handlerRecyclerView()
        initClicks()
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when(view.id){
            binding.buttonBack.id -> {

            }
            binding.buttonCall.id -> {

            }
            binding.buttonSendMessage.id -> {
                val calendar = Calendar.getInstance().time
                val formatter = SimpleDateFormat("HH:mm")
                val currentTimeFormated = formatter.format(calendar)
                val message = MessageModel(
                    binding.inputMessage.text.toString(),
                    firebaseAuthentication.currentUser?.uid.toString(),
                    navigationArguments.contactData.userId,
                    currentTimeFormated
                )
                if(message.message!!.isNotEmpty()){
                    viewModel.sendMessage(message, requireContext())
                }
            }
        }
    }

    private fun observers(){
        viewModel.sendMessageStatus.observe(this) { sendedMessageStatus ->
            if(sendedMessageStatus.status){
                binding.inputMessage.text.clear()
            }
        }
        viewModel.messageList.observe(this){messageList ->
            chatMessagesAdapter.getMessagesList(messageList)
        }
    }

    private fun initClicks(){
        binding.buttonBack.setOnClickListener(this)
        binding.buttonCall.setOnClickListener(this)
        binding.buttonSendMessage.setOnClickListener(this)
    }

    private fun contactData(){
        binding.contactName.text = navigationArguments.contactData.name
        Picasso.get().load(navigationArguments.contactData.profilePictureLink).into(binding.contactPhoto)
    }

    private fun handlerRecyclerView(){
        val senderRoom = navigationArguments.contactData.userId + firebaseAuthentication.currentUser?.uid.toString()
        viewModel.getMessages(senderRoom, requireContext())
        chatMessagesAdapter = ChatMessagesAdapter(requireContext(), firebaseAuthentication.currentUser?.uid.toString())
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.messageRecyclerView.adapter = chatMessagesAdapter
    }
}