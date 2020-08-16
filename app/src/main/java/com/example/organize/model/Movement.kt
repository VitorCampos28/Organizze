package com.example.organize.model

import com.example.organize.config.ConfigFirebase
import com.example.organize.helper.Base64Custom
import com.example.organize.helper.DateUtil
import com.google.firebase.database.DatabaseReference

class Movement {
    var date:String = ""
    var category:String = ""
    var description: String = ""
    var type:String = ""
    var value:Double = 0.00


    fun save(date:String){
        var authentication = ConfigFirebase.getFirebaseAuthentication()
        var emailUser = Base64Custom.crypto64(authentication.currentUser!!.email)
        var mounthYear = DateUtil.dateSelected(date)
        var firebase:DatabaseReference = ConfigFirebase.getFirebaseDatabase()
        firebase.child("Movement").child(emailUser).child(mounthYear).push().setValue(this)
    }
}