package com.example.organize.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.organize.R
import com.example.organize.adapter.AdapterMovement
import com.example.organize.config.ConfigFirebase
import com.example.organize.model.Movement
import com.example.organize.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.content_main_screen.*
import java.text.DecimalFormat


class MainScreenActivity : AppCompatActivity() {
    private lateinit var authentication: FirebaseAuth
    private var totalUserBill:Double = 0.00
    private var totalUserRecive:Double = 0.00
    private var balanceUser:Double = 0.00
    private lateinit var databaseRef:DatabaseReference
    private lateinit var databaseEventListener:ValueEventListener
    private lateinit var movesDatabaseEventListener:ValueEventListener
    private var movesRef = ConfigFirebase.getMovesInfo()
    private lateinit var adapterMovement:AdapterMovement
    private  var movesList = mutableListOf<Movement>()
    private lateinit var movement : Movement
    private var monthYear:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(findViewById(R.id.toolbar))
        dateUser()
        swipe()
    }
    fun swipe(){
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteMovies(viewHolder)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun deleteMovies(viewHolder: RecyclerView.ViewHolder){

        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluding account movement")
        alertDialog.setMessage("ARE YOU SURE ?")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirm" , DialogInterface.OnClickListener { dialogInterface, i ->
            var position = viewHolder.adapterPosition
            movement = movesList[position]
            movesRef = ConfigFirebase.getMovesInfo().child(monthYear)
            movesRef.child(movement.key).removeValue()
            adapterMovement.notifyItemRemoved(position)
            balanceUpdate()
        })

        alertDialog.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { _, _ ->
                Toast.makeText(
                    this,
                    "Canceled",
                    Toast.LENGTH_SHORT
                ).show()
                adapterMovement.notifyDataSetChanged()
            })

        var alert = alertDialog.create()
        alert.show()
    }

    fun balanceUpdate(){
        var userRef = ConfigFirebase.getUserInfo()
        if (movement.type == "I"){
            totalUserRecive -= movement.value
            userRef.child("totalIncome").setValue(totalUserRecive)
        }
        if (movement.type == "D"){
            totalUserBill -= movement.value
            userRef.child("totalBill").setValue(totalUserBill)
        }
    }

    override fun onStart() {
        super.onStart()
        recoveryUserInfo()
        recoveryMoves()
        recyclerView()
    }

    private fun recoveryUserInfo(){
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
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun bill(view: View){
        startActivity(Intent(this, BillActivity::class.java))
    }

    fun income(view: View){
        startActivity(Intent(this, IncomeActivity::class.java))
    }

    private fun recyclerView(){
        //Config Adapter
        adapterMovement = AdapterMovement(movesList, this)
        //Config RecyclerView
        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterMovement
    }

    private fun recoveryMoves(){
        movesRef = ConfigFirebase.getMovesInfo().child(monthYear)
        movesDatabaseEventListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movesList.clear()
                for(data:DataSnapshot in dataSnapshot.children) {
                    val post = data.getValue(Movement::class.java)
                    post!!.key = data.key.toString()
                    movesList.add(post)

                }
                adapterMovement.notifyDataSetChanged()

            }
        }
        movesRef.addValueEventListener(movesDatabaseEventListener)
    }

    private fun dateUser(){
        calendarView.setOnMonthChangedListener { _, _ ->
            var date:CalendarDay = calendarView.currentDate
            var selectMonth = String.format("%02d", (date.month + 1))
            monthYear =  selectMonth + "" + date.year.toString()
            movesRef.removeEventListener(movesDatabaseEventListener)
            recoveryMoves()
        }
        var date:CalendarDay = calendarView.currentDate
        var selectMonth = String.format("%02d", (date.month + 1))
        monthYear =  selectMonth + "" + date.year.toString()
    }

    override fun onStop() {
        super.onStop()
        databaseRef.removeEventListener(databaseEventListener)
        movesRef.removeEventListener(movesDatabaseEventListener)
    }
}