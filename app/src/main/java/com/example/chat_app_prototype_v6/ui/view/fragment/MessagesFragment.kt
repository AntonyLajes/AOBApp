package com.example.chat_app_prototype_v6.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app_prototype_v6.databinding.FragmentMessagesBinding
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.ui.view.adapter.MessageAdapter
import com.example.chat_app_prototype_v6.ui.viewmodel.MessagesViewModel
import com.example.chat_app_prototype_v6.util.datamodel.LastMessageModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding: FragmentMessagesBinding get() = _binding!!
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var viewModel: MessagesViewModel
    private var firebaseAuthentication = FirebaseInstance.getAuthenticationInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)
        viewModel.getContactsWithConversationStarted(
            firebaseAuthentication.currentUser?.uid.toString(),
            requireContext()
        )
        lastMessageAdapterHandler()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun lastMessageAdapterHandler() {
        messageAdapter = MessageAdapter(
            firebaseAuthentication.currentUser?.uid.toString(),
            requireContext(),
            object : MessageAdapter.OnItemClickListener {
                override fun onItemClickListener(messageData: LastMessageModel, position: Int) {
                    val contactData = UserProfileModel(
                        messageData.userId,
                        "",
                        messageData.name,
                        "",
                        "",
                        messageData.profilePictureLink
                    )
                    findNavController().navigate(
                        MessagesFragmentDirections.actionNavMessagesToChatFragment(
                            contactData
                        )
                    )
                }
            })
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun observers() {
        viewModel.contactsWithConversation.observe(requireActivity()) { contactsWithConversation ->
            messageAdapter.getMessagesList(contactsWithConversation)
        }
    }
}