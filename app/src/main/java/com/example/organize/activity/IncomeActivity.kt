package com.example.organize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.organize.R
import com.example.organize.config.ConfigFirebase
import com.example.organize.helper.Base64Custom
import com.example.organize.helper.DateUtil
import com.example.organize.model.Movement
import com.example.organize.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bill.*
import kotlinx.android.synthetic.main.activity_income.*

class IncomeActivity : AppCompatActivity() {
    private lateinit var movement: Movement
    private var value = 0.00
    private var category:String = ""
    private var description:String = ""
    private var date: String = ""
    private var firebaseRef: DatabaseReference = ConfigFirebase.getFirebaseDatabase()
    private var autentication: FirebaseAuth = ConfigFirebase.getFirebaseAuthentication()
    private var totalUserIncome:Double = 0.00
    private var newIncome:Double = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        dateIncome.setText(DateUtil.currentDate())
    }

    fun saveIncome(view : View){
        if (validFieldIncome()) {
            movement.value = value
            movement.category = category
            movement.description = description
            movement.date = date
            movement.type = "I"
            movement.save(date)
            recoveryIncome()
            updateTotalIncome(newIncome)
        }
    }

    private fun validFieldIncome():Boolean{
        movement = Movement()
        if (!totalIncome.text.isNullOrEmpty()){
            value = totalIncome.text.toString().toDouble()
        }
        category = categoryIncome.text.toString()
        description = descriptionIncome.text.toString()
        date = dateIncome.text.toString()
        newIncome = totalUserIncome + value

        when{
            totalIncome.text.isNullOrEmpty()-> Toast.makeText(this, "Invalid value", Toast.LENGTH_SHORT).show()
            category.isEmpty() -> Toast.makeText(this, "Write the category", Toast.LENGTH_SHORT).show()
            description.isEmpty() -> Toast.makeText(this, "Write the description", Toast.LENGTH_SHORT).show()
            date.isEmpty() -> Toast.makeText(this, "Write the date", Toast.LENGTH_SHORT).show()
            else -> return true
        }

        return false

    }

    private fun recoveryIncome() {
        var emailUser = autentication.currentUser!!.email
        var idUser: String = Base64Custom.crypto64(emailUser)
        var userRef = firebaseRef.child("Users").child(idUser)

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(Users::class.java)
                totalUserIncome = post!!.totalIncome
            }
        }

        userRef.addValueEventListener(postListener)
    }

    fun updateTotalIncome(income:Double){
        var emailUser = autentication.currentUser!!.email
        var idUser: String = Base64Custom.crypto64(emailUser)
        var userRef = firebaseRef.child("Users").child(idUser)

        userRef.child("totalIncome").setValue(income)
    }
}