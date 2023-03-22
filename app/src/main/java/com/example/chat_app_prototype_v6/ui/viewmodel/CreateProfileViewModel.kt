package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.StatusModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel

class CreateProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var storageDatabase = FirebaseInstance.getStorageInstance()
    private var realtimeDatabase = FirebaseInstance.getRealtimeDatabaseInstance()
    private var _uploadDataStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val uploadDataStatus: LiveData<StatusModel> get() = _uploadDataStatus

    fun saveProfile(profilePicture: ByteArray, userProfile: UserProfileModel, context: Context) {
        val storageReference = storageDatabase.reference.child("profilePictures/$profilePicture.jpeg")
        storageReference.putBytes(profilePicture).addOnCompleteListener { uploadProfilePicture ->
            if (uploadProfilePicture.isSuccessful) {
                storageReference.downloadUrl.addOnCompleteListener { profilePictureLink ->
                    if (profilePictureLink.isSuccessful) {
                        userProfile.profilePictureLink = profilePictureLink.result.toString()
                        realtimeDatabase.child(context.getString(R.string.users)).child(userProfile.userId.toString()).setValue(userProfile).addOnCompleteListener { uploadUserDataTask ->
                            if(uploadUserDataTask.isSuccessful){
                                _uploadDataStatus.value = StatusModel()
                            }else{
                                _uploadDataStatus.value = StatusModel(false, uploadUserDataTask.exception?.message ?: context.getString(R.string.error))
                            }
                        }
                    }
                }
            } else {
                _uploadDataStatus.value = StatusModel(false, uploadProfilePicture.exception?.message ?: context.getString(R.string.error))
            }
        }
    }
}