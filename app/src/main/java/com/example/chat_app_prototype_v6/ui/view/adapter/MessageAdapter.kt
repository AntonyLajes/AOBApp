package com.example.chat_app_prototype_v6.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.databinding.MessageItemBinding
import com.example.chat_app_prototype_v6.util.datamodel.LastMessageModel
import com.squareup.picasso.Picasso

class MessageAdapter(var currentUserId: String, var context: Context, var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    var lastMessageList: ArrayList<LastMessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messageData = lastMessageList[position]
        holder.messageItem.contactName.text = messageData.name
        holder.messageItem.contactLastMessage.text = messageData.lastMessage
        holder.messageItem.lastMessageTime.text = messageData.lastMessageTime
        if(messageData.userId != currentUserId){
            holder.messageItem.messageIcon.setImageDrawable(context.getDrawable(R.drawable.ic_sended))
        }else{
            holder.messageItem.messageIcon.setImageDrawable(context.getDrawable(R.drawable.ic_received))
        }
        Picasso.get().load(messageData.profilePictureLink).into(holder.messageItem.contactPhoto)
        holder.messageItem.messageField.setOnClickListener {
            onItemClickListener.onItemClickListener(messageData, position)
        }
    }

    override fun getItemCount(): Int = lastMessageList.size

    fun getMessagesList(data: ArrayList<LastMessageModel>){
        lastMessageList = data
        notifyDataSetChanged()
    }

    class MyViewHolder(val messageItem: MessageItemBinding) : RecyclerView.ViewHolder(messageItem.root)

    interface OnItemClickListener{
        fun onItemClickListener(messageData: LastMessageModel, position: Int)
    }
}