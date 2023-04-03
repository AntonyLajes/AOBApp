package com.example.chat_app_prototype_v6.ui.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.FragmentChatBinding
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
    private val callPhoneRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { request ->
            if (request) {
                makeCallPhone()
            }
        }

    companion object {
        const val CALL_PHONE_PERMISSION = Manifest.permission.CALL_PHONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.button_call -> {
                        verifyCallPhonePermission()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
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
        when (view.id) {
            binding.toolbar.id -> {
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
                if (message.message!!.isNotEmpty()) {
                    viewModel.sendMessage(message, requireContext())
                }
            }
        }
    }

    private fun observers() {
        viewModel.sendMessageStatus.observe(this) { sendedMessageStatus ->
            if (sendedMessageStatus.status) {
                binding.inputMessage.text.clear()
            }
        }
        viewModel.messageList.observe(this) { messageList ->
            chatMessagesAdapter.getMessagesList(messageList)
        }
    }

    private fun initClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSendMessage.setOnClickListener(this)
    }

    private fun contactData() {
        binding.contactName.text = navigationArguments.contactData.name
        Picasso.get().load(navigationArguments.contactData.profilePictureLink)
            .into(binding.contactPhoto)
    }

    private fun handlerRecyclerView() {
        val senderRoom =
            navigationArguments.contactData.userId + firebaseAuthentication.currentUser?.uid.toString()
        viewModel.getMessages(senderRoom, requireContext())
        chatMessagesAdapter = ChatMessagesAdapter(
            requireContext(),
            firebaseAuthentication.currentUser?.uid.toString()
        )
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.messageRecyclerView.adapter = chatMessagesAdapter
    }

    private fun verifyPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

    private fun verifyCallPhonePermission() {
        val callPhonePermissionAccepted = verifyPermission(CALL_PHONE_PERMISSION)

        when {
            callPhonePermissionAccepted -> {
                makeCallPhone()
            }
            shouldShowRequestPermissionRationale(CALL_PHONE_PERMISSION) -> {

            }
            else -> {
                callPhoneRequest.launch(CALL_PHONE_PERMISSION)
            }
        }
    }

    private fun makeCallPhone() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel: ${navigationArguments.contactData.phoneNumber}")
        startActivity(intent)
    }
}