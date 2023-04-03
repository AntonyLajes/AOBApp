package com.example.chat_app_prototype_v6.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app_prototype_v6.databinding.ReceiverItemBinding
import com.example.chat_app_prototype_v6.databinding.SenderItemBinding
import com.example.chat_app_prototype_v6.util.datamodel.MessageModel

class ChatMessagesAdapter(val context: Context, private val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messagesList: ArrayList<MessageModel> = ArrayList()

    companion object {
        const val MESSAGE_SENDED = 1
        const val MESSAGE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MESSAGE_SENDED) {
            val view = SenderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SenderViewHolder(view)
        } else {
            val view =
                ReceiverItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]
        return if (currentMessage.senderId == currentUserId) {
            MESSAGE_SENDED
        } else {
            MESSAGE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.item.message.text = currentMessage.message
            viewHolder.item.messageTime.text = currentMessage.time
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.item.message.text = currentMessage.message
            viewHolder.item.messageTime.text = currentMessage.time
        }
    }

    fun getMessagesList(list: ArrayList<MessageModel>) {
        messagesList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = messagesList.size

    class SenderViewHolder(var item: SenderItemBinding) : RecyclerView.ViewHolder(item.root)
    class ReceiverViewHolder(var item: ReceiverItemBinding) : RecyclerView.ViewHolder(item.root)
}