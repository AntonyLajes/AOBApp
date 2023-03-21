package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.StatusModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel

class CreateProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = FirebaseInstance.getAuthenticationInstance()
    private var storageDatabase = FirebaseInstance.getStorageInstance()
    private var _uploadDataStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val uploadDataStatus: LiveData<StatusModel> get() = _uploadDataStatus

    fun saveProfile(profilePicture: ByteArray, userProfile: UserProfileModel) {
        val storageReference = storageDatabase.reference.child("profilePictures/$profilePicture.jpeg")
        storageReference.putBytes(profilePicture).addOnCompleteListener { uploadProfilePicture ->
            if (uploadProfilePicture.isSuccessful) {
                storageReference.downloadUrl.addOnCompleteListener { profilePictureLink ->
                    if (profilePictureLink.isSuccessful) {
                        userProfile.profilePictureLink = profilePictureLink.result.toString()
                        userProfile.phoneNumber = auth.currentUser?.phoneNumber ?: ""

                    }
                }
            } else {
                StatusModel(false, uploadProfilePicture.exception?.message ?: "Error")
            }
        }
    }
}