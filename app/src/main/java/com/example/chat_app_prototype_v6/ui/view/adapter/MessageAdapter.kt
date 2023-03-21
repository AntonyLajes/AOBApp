package com.example.chat_app_prototype_v6.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app_prototype_v6.databinding.MessageItemBinding
import com.example.chat_app_prototype_v6.util.datamodel.LastMessageModel
import com.squareup.picasso.Picasso

class MessageAdapter(var context: Context) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    private var lastMessageList: ArrayList<LastMessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messageData = lastMessageList[position]
        holder.messageItem.contactName.text = messageData.name
        holder.messageItem.contactLastMessage.text = messageData.lastMessage
        holder.messageItem.lastMessageTime.text = messageData.lastMessageTime
        Picasso.with(context).load(messageData.profilePictureLink).into(holder.messageItem.contactPhoto)
        //Glide.with(context).load(messageData.profilePictureLink).skipMemoryCache(true).into(holder.messageItem.contactPhoto)
    }

    override fun getItemCount(): Int = lastMessageList.size

    fun getLastMessageList(messageList: ArrayList<LastMessageModel>) {
        lastMessageList = messageList
        notifyDataSetChanged()
    }

    class MyViewHolder(val messageItem: MessageItemBinding) : RecyclerView.ViewHolder(messageItem.root)
}