package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.MessageModel
import com.example.chat_app_prototype_v6.util.datamodel.StatusModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val realtimeDatabase = FirebaseInstance.getRealtimeDatabaseInstance()
    private var _sendMessageStatus: MutableLiveData<StatusModel> = MutableLiveData()
    val sendMessageStatus: LiveData<StatusModel> get() = _sendMessageStatus
    private var _messageList: MutableLiveData<ArrayList<MessageModel>> = MutableLiveData()
    val messageList: LiveData<ArrayList<MessageModel>> get() = _messageList

    fun sendMessage(message: MessageModel, context: Context) {
        val senderRoom = message.receiverId + message.senderId
        val receiverRoom = message.senderId + message.receiverId
        realtimeDatabase
            .child(context.getString(R.string.messages_database))
            .child(senderRoom)
            .child(context.getString(R.string.messages_database))
            .push()
            .setValue(message)
            .addOnCompleteListener { senderUploadMessageTask ->
                if (senderUploadMessageTask.isSuccessful) {
                    realtimeDatabase
                        .child(context.getString(R.string.messages_database))
                        .child(receiverRoom)
                        .child(context.getString(R.string.messages_database))
                        .push()
                        .setValue(message).addOnCompleteListener { receiverUploadMessageTask ->
                            if(receiverUploadMessageTask.isSuccessful){
                                _sendMessageStatus.value = StatusModel()
                            }else{
                                _sendMessageStatus.value = StatusModel(false, senderUploadMessageTask.exception?.message.toString())
                            }
                        }
                } else {
                    _sendMessageStatus.value = StatusModel(false, senderUploadMessageTask.exception?.message.toString())
                }
            }
    }

    fun getMessages(senderRoom: String, context: Context){
        val messageList: ArrayList<MessageModel> = ArrayList()
        realtimeDatabase
            .child(context.getString(R.string.messages_database))
            .child(senderRoom)
            .child(context.getString(R.string.messages_database))
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    _messageList.value?.clear()
                    for(postSnapshot in snapshot.children){
                        val currentMessage = postSnapshot.getValue(MessageModel::class.java)
                        messageList.add(currentMessage!!)
                    }
                    _messageList.value = messageList
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}