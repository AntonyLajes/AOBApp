package com.example.chat_app_prototype_v6.ui.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.FragmentContactsBinding
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.ui.view.adapter.ContactAdapter
import com.example.chat_app_prototype_v6.ui.viewmodel.ContactsViewModel
import com.example.chat_app_prototype_v6.util.datamodel.ContactModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!
    private val contactList: ArrayList<ContactModel> = ArrayList()
    private val firebaseAuthentication = FirebaseInstance.getAuthenticationInstance()
    private lateinit var viewModel: ContactsViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var contactAdapter: ContactAdapter
    private val contactListRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                getContactsList()
            } else {
                showAlertDialogPermission()
            }
        }

    companion object {
        const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        verifyGalleryPermission()
        handlerRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getContactsList(contactList, requireContext(), firebaseAuthentication.currentUser?.uid.toString())
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers(){
        viewModel.contactList.observe(requireActivity()) { matchedContactList ->
            contactAdapter.getContactList(matchedContactList)
            contactAdapter.notifyDataSetChanged()
        }
    }

    private fun verifyPermissions(permission: String): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

    private fun verifyGalleryPermission() {
        val readContactsPermissionAccepted = verifyPermissions(READ_CONTACTS_PERMISSION)

        when {
            readContactsPermissionAccepted -> {
                getContactsList()
            }
            shouldShowRequestPermissionRationale(READ_CONTACTS_PERMISSION) -> {
                showAlertDialogPermission()
            }
            else -> {
                contactListRequest.launch(READ_CONTACTS_PERMISSION)
            }
        }
    }

    private fun getContactsList() {
        val contentResolver = requireContext().contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        while (cursor!!.moveToNext()) {
            val contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contactList.add(ContactModel(contactName, contactNumber))
        }
        cursor.close()
    }

    private fun showAlertDialogPermission() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.permission_dialog_read_contacts_message))
            .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                alertDialog.dismiss()
            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireContext().packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                alertDialog.dismiss()
            }
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun handlerRecyclerView(){
        contactAdapter = ContactAdapter(object: ContactAdapter.OnItemClickListener{
            override fun onItemClickListener(contactData: UserProfileModel, position: Int) {

            }
        }, requireContext())
        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.contactsRecyclerView.adapter = contactAdapter
    }
}