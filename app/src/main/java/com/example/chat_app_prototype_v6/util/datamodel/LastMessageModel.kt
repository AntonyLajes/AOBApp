package com.example.chat_app_prototype_v6.util.datamodel

data class LastMessageModel(
    var name: String,
    var userId: String,
    var profilePictureLink: String,
    var lastMessage: String,
    var lastMessageTime: String
)
