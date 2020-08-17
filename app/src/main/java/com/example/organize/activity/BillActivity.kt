package com.example.organize.activity

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.content_main_screen.*
import java.text.DecimalFormat

class BillActivity : AppCompatActivity() {
    private lateinit var movement: Movement
    private var value = 0.00
    private var category:String = ""
    private var description:String = ""
    private var date: String = ""
    private var totalUserBill:Double = 0.00
    private var newBill:Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        recoveryBill()
        //Auto write the field Data
        dateBill.setText(DateUtil.currentDate())
    }

    fun saveBill(view: View){
        if (validFieldBill()) {
            movement.value = value
            movement.category = category
            movement.description = description
            movement.date = date
            movement.type = "D"
            movement.save(date)
            updateTotalBill(newBill)
            onCreateDialog().show()
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

        var dataBaseRef = ConfigFirebase.getUserInfo()

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(Users::class.java)
                totalUserBill = post!!.totalBill

            }
        }
        dataBaseRef.addValueEventListener(postListener)
    }

    fun updateTotalBill(bill:Double){
        var userRef = ConfigFirebase.getUserInfo()

        userRef.child("totalBill").setValue(bill)
    }
     fun onCreateDialog(): Dialog {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(this)
            builder.setMessage("You want leave")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
         return builder.create()
    }
}