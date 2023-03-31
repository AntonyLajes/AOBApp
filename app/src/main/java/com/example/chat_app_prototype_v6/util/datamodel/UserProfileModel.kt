package com.example.chat_app_prototype_v6.util.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileModel(
    var userId: String = "",
    var phoneNumber: String = "",
    var name: String = "",
    var user_name: String = "",
    var status: String = "",
    var profilePictureLink: String = ""
): Parcelable
