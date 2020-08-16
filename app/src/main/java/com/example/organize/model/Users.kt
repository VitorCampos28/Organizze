package com.example.organize.model

import com.example.organize.config.ConfigFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude

class Users {

      var name: String? = null
      var email: String? = null
      @get: Exclude
      var password: String? = null
      @get: Exclude
      var idName: String? = null
      var totalIncome:Double = 0.00
      var totalBill:Double = 0.00




      fun save() {
            var fireBase: DatabaseReference = ConfigFirebase.getFirebaseDatabase()
            fireBase.child("Users").child(this.idName.toString()).setValue(this);
      }
}