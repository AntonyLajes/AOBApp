package com.example.chat_app_prototype_v6.util.datamodel

data class MessageModel(
    var message: String? = "",
    var senderId: String? = "",
    var receiverId: String? = "",
    var time: String? = "",
)
