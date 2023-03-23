package com.example.chat_app_prototype_v6.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app_prototype_v6.databinding.ContactItemBinding
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel
import com.squareup.picasso.Picasso

class ContactAdapter(var onItemClickListener: OnItemClickListener, var context: Context): RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    private var contactList: ArrayList<UserProfileModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contactData = contactList[position]
        holder.contactItem.contactName.text = contactData.name
        holder.contactItem.contactStatus.text = contactData.status
        Picasso.with(context).load(contactData.profilePictureLink).into(holder.contactItem.contactPhoto)
        holder.contactItem.contactField.setOnClickListener {
            onItemClickListener.onItemClickListener(contactData, position)
        }
    }

    override fun getItemCount(): Int = contactList.size

    fun getContactList(list: ArrayList<UserProfileModel>){
        contactList = list
    }

    class MyViewHolder(val contactItem: ContactItemBinding) : RecyclerView.ViewHolder(contactItem.root)

    interface OnItemClickListener{
        fun onItemClickListener(contactData: UserProfileModel, position: Int)
    }
}