package com.example.chat_app_prototype_v6.ui.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FirebaseInstance private constructor(){

    companion object{
        private lateinit var AUTH_INSTANCE: FirebaseAuth
        private lateinit var FIREBASE_INSTANCE: FirebaseFirestore
        private lateinit var FIREBASE_STORAGE: FirebaseStorage
        private lateinit var FIREBASE_REALTIME_DB: DatabaseReference

        fun getAuthenticationInstance(): FirebaseAuth{
            if(!::AUTH_INSTANCE.isInitialized){
                AUTH_INSTANCE = FirebaseAuth.getInstance()
            }
            return AUTH_INSTANCE
        }

        fun getRealtimeDatabaseInstance(): DatabaseReference{
            if(!::FIREBASE_REALTIME_DB.isInitialized){
                FIREBASE_REALTIME_DB = FirebaseDatabase.getInstance().reference
            }
            return FIREBASE_REALTIME_DB
        }

        fun getFirestoreInstance(): FirebaseFirestore {
            if(!::FIREBASE_INSTANCE.isInitialized){
                FIREBASE_INSTANCE = Firebase.firestore
            }
            return FIREBASE_INSTANCE
        }

        fun getStorageInstance(): FirebaseStorage {
            if(!::FIREBASE_STORAGE.isInitialized){
                FIREBASE_STORAGE = Firebase.storage
            }
            return FIREBASE_STORAGE
        }
    }

}