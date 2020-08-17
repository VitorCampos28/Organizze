package com.example.organize.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.organize.R
import com.example.organize.config.ConfigFirebase
import com.example.organize.helper.Base64Custom
import com.example.organize.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.content_main_screen.*
import java.text.DecimalFormat

class MainScreenActivity : AppCompatActivity() {
    private lateinit var authentication: FirebaseAuth
    private var totalUserBill:Double = 0.00
    private var totalUserRecive:Double = 0.00
    private var balanceUser:Double = 0.00
    private lateinit var databaseRef:DatabaseReference
    private lateinit var databaseEventListener:ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(findViewById(R.id.toolbar))
        calendarView.setOnMonthChangedListener { _, date ->
            Log.i("Date", (date.month + 1).toString() + "/" + date.year)
        }
    }

    override fun onStart() {
        super.onStart()
        recoveryUserInfo()
        Log.i("Event1" , "Start Event")
    }

    fun recoveryUserInfo(){
        databaseRef = ConfigFirebase.getUserInfo()

            databaseEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(Users::class.java)
                totalUserBill = post!!.totalBill.toDouble()
                totalUserRecive = post!!.totalIncome.toDouble()
                balanceUser = totalUserRecive - totalUserBill

                var decimalFormat:DecimalFormat = DecimalFormat("0.##")
                var formatResult = decimalFormat.format(balanceUser)

                textWelcome.text = "Hello, " + post.name
                textBalance.text = "R$: $formatResult"
            }
        }

        databaseRef.addValueEventListener(databaseEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuLeave -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(){
        authentication = ConfigFirebase.getFirebaseAuthentication()
        authentication.signOut()
        startActivity(Intent(this , MainActivity::class.java))
        finish()
    }

    fun bill(view:View){
        startActivity(Intent(this , BillActivity::class.java ))
    }

    fun income(view:View){
        startActivity(Intent(this , IncomeActivity::class.java ))
    }

    override fun onStop() {
        super.onStop()
        Log.i("Event1" , "Stop Event")
        databaseRef.removeEventListener(databaseEventListener)
    }
}