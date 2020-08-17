package com.example.organize.config

import com.example.organize.helper.Base64Custom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfigFirebase {

    companion object{
        private var firebaseRef:DatabaseReference = getFirebaseDatabase()
        private var autentication:FirebaseAuth = getFirebaseAuthentication()
        private  var authentication: FirebaseAuth? = null
        private var referenceFireBase: DatabaseReference? = null

        //return Instance to FirebaseDatabase
        fun getFirebaseDatabase():DatabaseReference{
            if (referenceFireBase== null){
                referenceFireBase = FirebaseDatabase.getInstance().reference
            }
            return referenceFireBase as DatabaseReference
        }
        //return the firebase instance
        fun getFirebaseAuthentication():FirebaseAuth{
            if (authentication == null) {
                authentication = FirebaseAuth.getInstance()
            }
            return authentication!!
        }
        fun getUserInfo(): DatabaseReference {
            val emailUser = autentication.currentUser!!.email
            val idUser: String = Base64Custom.crypto64(emailUser)
            val userRef = firebaseRef.child("Users").child(idUser)
            return userRef
        }

    }

}