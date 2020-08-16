package com.example.organize.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.organize.R
import com.example.organize.config.ConfigFirebase
import com.example.organize.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editEmail
import kotlinx.android.synthetic.main.activity_login.editPassword
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private var user = Users()
    private lateinit var authentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            email = editEmail.text.toString()
            password = editPassword.text.toString()

            if (email.isEmpty()){
                Toast.makeText(this, "Write the Email", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()){
                Toast.makeText(this, "Write the password", Toast.LENGTH_SHORT).show()
            }else{
                user = Users()
                user.email = email
                user.password = password
                validLogin()
            }

        }
    }
    fun validLogin(){
        authentication = ConfigFirebase.getFirebaseAuthentication()
        authentication.signInWithEmailAndPassword(
            user.email.toString(),
            user.password.toString()
        ).addOnCompleteListener(this , OnCompleteListener {
            if (it.isSuccessful){
                loadMainScreen()
                Toast.makeText(this, "Successful signing in", Toast.LENGTH_SHORT).show()
            }else{
                var exception: String
                try {
                    throw it.exception!!
                }catch (e: FirebaseAuthInvalidUserException){
                    exception = "User is not Already registered"
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    exception = "Wrong password or E-mail"
                }catch (e: Exception){
                    exception = "Error signing in" + e.message
                }
                Toast.makeText(this, exception, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun loadMainScreen(){
        startActivity(Intent(this , MainScreenActivity::class.java))
        finish()
    }
}