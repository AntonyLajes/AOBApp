package com.example.chat_app_prototype_v6.ui.view.activity

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
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.ActivityCreateProfileBinding
import com.example.chat_app_prototype_v6.databinding.ActivityMainBinding
import com.example.chat_app_prototype_v6.ui.viewmodel.CreateProfileViewModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel
import java.io.ByteArrayOutputStream

class CreateProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var alertDialog: AlertDialog
    private var bitmap: Bitmap? = null
    private lateinit var viewmodel: CreateProfileViewModel
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
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        applicationContext.contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                binding.selectProfilePhoto.setImageBitmap(bitmap)
            }
        }

    companion object {
        private const val GALLERY_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this).get(CreateProfileViewModel::class.java)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initClicks()
        observers()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.selectProfilePhoto.id -> {
                verifyGalleryPermission()
            }
            binding.buttonCreateProfile.id -> {
                val byteArrayOutputStream = ByteArrayOutputStream()
                if(bitmap != null){
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                }
                val profilePictureData = byteArrayOutputStream.toByteArray()
                val name = binding.inputName.text.toString()
                val userName = binding.inputUsername.unMasked
                val status = binding.inputStatus.text.toString()
                val userData = UserProfileModel(
                    null,
                    name,
                    userName,
                    status,
                    null
                )
                when{
                    name.isEmpty() || userName.isEmpty() || status.isEmpty() -> {
                        makeToast(getString(R.string.insert_data_error))
                    }
                    bitmap == null -> {
                        makeToast(getString(R.string.insert_profile_picture))
                    }
                    else -> {
                        viewmodel.saveProfile(profilePictureData, userData)
                    }
                }
            }
        }
    }

    private fun observers() {
        viewmodel.uploadDataStatus.observe(this) { uploadData ->
            if (uploadData.status) {
                makeToast(getString(R.string.signup_success))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                makeToast(uploadData.message)
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initClicks() {
        binding.selectProfilePhoto.setOnClickListener(this)
        binding.buttonCreateProfile.setOnClickListener(this)
    }

    private fun verifyPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            applicationContext,
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
        val builder = AlertDialog.Builder(applicationContext)
            .setTitle("Atenção")
            .setMessage("Permita o acesso a galeria!")
            .setNegativeButton("Não") { _, _ ->
                alertDialog.dismiss()
            }
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName ?: "", null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                alertDialog.dismiss()
            }
        alertDialog = builder.create()
        alertDialog.show()
    }
}