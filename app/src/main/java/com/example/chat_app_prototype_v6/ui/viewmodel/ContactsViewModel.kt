package com.example.chat_app_prototype_v6.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chat_app_prototype_v6.R
import com.example.chat_app_prototype_v6.ui.model.FirebaseInstance
import com.example.chat_app_prototype_v6.util.datamodel.ContactModel
import com.example.chat_app_prototype_v6.util.datamodel.UserProfileModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val realtimeDatabase = FirebaseInstance.getRealtimeDatabaseInstance()
    private val databaseContactList: ArrayList<UserProfileModel> = ArrayList()
    private val contactExistsList: ArrayList<UserProfileModel> = ArrayList()
    private var _contactList: MutableLiveData<ArrayList<UserProfileModel>> = MutableLiveData()
    val contactList: LiveData<ArrayList<UserProfileModel>> get() = _contactList

    fun getContactsList(contactList: ArrayList<ContactModel>, context: Context, currentUserId: String) {
        realtimeDatabase.child(context.getString(R.string.users))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(UserProfileModel::class.java)
                        databaseContactList.add(currentUser!!)
                    }

                    for (contact in contactList) {
                        for (contactDatabase in databaseContactList) {
                            if (contact.number == contactDatabase.phoneNumber) {
                                if (contactDatabase !in contactExistsList) {
                                    if(currentUserId != contactDatabase.userId){
                                        contactExistsList.add(contactDatabase)
                                    }
                                }
                            }
                        }
                    }
                    _contactList.value = contactExistsList
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}