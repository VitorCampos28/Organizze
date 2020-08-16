package com.example.organize.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfigFirebase {
    companion object{
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
    }

}