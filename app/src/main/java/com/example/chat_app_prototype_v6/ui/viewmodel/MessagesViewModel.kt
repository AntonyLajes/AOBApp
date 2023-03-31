package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.LastMessageModel
import com.example.chat_app_prototype_v6.util.datamodel.MessageModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel

class MessagesViewModel(application: Application) : AndroidViewModel(application) {

    private val realtimeDatabase = FirebaseInstance.getRealtimeDatabaseInstance()
    private var _contactsWithConversation: MutableLiveData<ArrayList<LastMessageModel>> =
        MutableLiveData()
    val contactsWithConversation: LiveData<ArrayList<LastMessageModel>> get() = _contactsWithConversation

    fun getContactsWithConversationStarted(currentUserId: String, context: Context) {
        val contactsAtDatabase: ArrayList<UserProfileModel> = ArrayList()
        val contactsWithConversationStarted: ArrayList<LastMessageModel> = ArrayList()

        realtimeDatabase
            .child(context.getString(R.string.users))
            .get()
            .addOnCompleteListener { usersIdTask ->
                if (usersIdTask.isSuccessful) {
                    for (user in usersIdTask.result.children) {
                        val currentUser = user.getValue(UserProfileModel::class.java)
                        contactsAtDatabase.add(currentUser!!)
                    }
                    contactsAtDatabase.forEach() { contact ->
                        if (contact.userId != currentUserId) {
                            val senderRoom = contact.userId + currentUserId
                            realtimeDatabase.child(context.getString(R.string.messages_database))
                                .child(senderRoom)
                                .child(context.getString(R.string.messages_database))
                                .get()
                                .addOnCompleteListener { snapshot ->
                                    if (snapshot.result.exists()) {
                                        val lastChildren = snapshot.result.children.last().getValue(MessageModel::class.java)
                                        if (contact.userId == lastChildren!!.receiverId) {
                                            val currentLastMessageModel = LastMessageModel(
                                                contact.name,
                                                contact.userId,
                                                contact.profilePictureLink,
                                                lastChildren.message ?: "",
                                                lastChildren.time ?: ""
                                            )
                                            contactsWithConversationStarted.add(
                                                currentLastMessageModel
                                            )
                                            _contactsWithConversation.value = contactsWithConversationStarted
                                        }
                                        println("success")
                                    }else{
                                        println(snapshot.exception?.message.toString())
                                    }
                                }
                        }
                    }
                }
            }
    }
}