package com.example.chat_app_prototype_v6.ui.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.chat_app_prototype_v6.databinding.FragmentCreateProfileBinding
import com.example.chat_app_prototype_v6.ui.view.activity.MainActivity

class CreateProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding: FragmentCreateProfileBinding get() = _binding!!
    private lateinit var alertDialog: AlertDialog
    private val galleryRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                galleryResult.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermission()
            }
        }
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data?.data != null) {
                val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        requireContext().contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                binding.selectProfilePhoto.setImageBitmap(bitmap)
            }
        }

    companion object {
        private val GALLERY_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initClicks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        when(view.id){
            binding.selectProfilePhoto.id -> {
                verifyGalleryPermission()
            }
            binding.buttonCreateProfile.id -> {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    private fun initClicks(){
        binding.selectProfilePhoto.setOnClickListener(this)
        binding.buttonCreateProfile.setOnClickListener(this)
    }

    private fun verifyPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

    private fun verifyGalleryPermission() {
        val permissionAccepted = verifyPermission(GALLERY_PERMISSION)
        when {
            permissionAccepted -> {
                galleryResult.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(GALLERY_PERMISSION) -> {
                showDialogPermission()
            }
            else -> {
                galleryRequest.launch(GALLERY_PERMISSION)
            }
        }
    }

    private fun showDialogPermission() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Atenção")
            .setMessage("Permita o acesso a galeria!")
            .setNegativeButton("Não") { _, _ ->
                alertDialog.dismiss()
            }
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context?.packageName ?: "", null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                alertDialog.dismiss()
            }
        alertDialog = builder.create()
        alertDialog.show()
    }
}