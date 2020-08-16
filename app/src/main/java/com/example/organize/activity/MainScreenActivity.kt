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
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.content_main_screen.*

class MainScreenActivity : AppCompatActivity() {
    private lateinit var authentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(findViewById(R.id.toolbar))
        calendarView.setOnMonthChangedListener { widget, date ->
            Log.i("Date", (date.month + 1).toString() + "/" + date.year)
        }
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

    fun bill(view :View){
        startActivity(Intent(this , BillActivity::class.java ));
    }

    fun income(view: View){
        startActivity(Intent(this , IncomeActivity::class.java ));
    }

}