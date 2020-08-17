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

class BillActivity : AppCompatActivity() {
    private lateinit var movement: Movement
    private var value = 0.00
    private lateinit var userInfo:ConfigFirebase
    private var category:String = ""
    private var description:String = ""
    private var date: String = ""
    private var totalUserBill:Double = 0.00
    private var newBill:Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        //Auto write the field Data
        dateBill.setText(DateUtil.currentDate())
    }

    fun saveBill(){
        if (validFieldBill()) {
            movement.value = value
            movement.category = category
            movement.description = description
            movement.date = date
            movement.type = "D"
            movement.save(date)
            recoveryBill()
            updateTotalBill(newBill)
        }
    }

    private fun validFieldBill():Boolean{
        movement = Movement()
        if (!totalBill.text.isNullOrEmpty()){
            value = totalBill.text.toString().toDouble()
        }
        category = categoryBill.text.toString()
        description = descriptionBill.text.toString()
        date = dateBill.text.toString()
        newBill = totalUserBill + value

        when{
            totalBill.text.isNullOrEmpty()-> Toast.makeText(this, "Invalid value", Toast.LENGTH_SHORT).show()
            category.isEmpty() -> Toast.makeText(this, "Write the category", Toast.LENGTH_SHORT).show()
            description.isEmpty() -> Toast.makeText(this, "Write the description", Toast.LENGTH_SHORT).show()
            date.isEmpty() -> Toast.makeText(this, "Write the date", Toast.LENGTH_SHORT).show()
            else -> return true
        }

        return false

    }

    private fun recoveryBill() {
        val userRef = ConfigFirebase.getUserInfo()

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(Users::class.java)
                totalUserBill = post!!.totalBill
            }
        }

        userRef.addValueEventListener(postListener)
    }

    fun updateTotalBill(bill:Double){
        val userRef = ConfigFirebase.getUserInfo()

        userRef.child("totalBill").setValue(bill)
    }

}