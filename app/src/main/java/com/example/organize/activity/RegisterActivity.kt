package com.example.organize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.organize.R
import com.example.organize.config.ConfigFirebase
import com.example.organize.helper.Base64Custom
import com.example.organize.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    lateinit var authentication: FirebaseAuth
    private  var user = Users()
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister.setOnClickListener {
            if(validFields()){
                user = Users()
                user.name = name
                user.email = email
                user.password = password
                registerUser()
            }
        }

    }
    private fun validFields():Boolean{
        name = editName.text.toString()
        email = editEmail.text.toString()
        password = editPassword.text.toString()
        when{
            name.isEmpty() -> Toast.makeText(this, "Write the name", Toast.LENGTH_SHORT).show()
            email.isEmpty() -> Toast.makeText(this, "Write the email", Toast.LENGTH_SHORT).show()
            password.isEmpty() -> Toast.makeText(this, "Write the password", Toast.LENGTH_SHORT).show()
            else -> return true
        }
        return false
    }

    fun registerUser(){
        authentication = ConfigFirebase.getFirebaseAuthentication()
        authentication.createUserWithEmailAndPassword(
            user.email.toString() , user.password.toString()
        ).addOnCompleteListener(this , OnCompleteListener {
            if (it.isSuccessful){
                var idUser = Base64Custom.crypto64(user.email.toString())
                user.idName = idUser

                user.save()
                finish()
            }else{
                var exception:String = ""
                try {
                    throw it.exception!!
                }catch (e: FirebaseAuthWeakPasswordException){
                    exception = "Write a strongest password"
                }catch(e: FirebaseAuthInvalidCredentialsException){
                    exception = "Write a valid e-mail"
                }catch (e: FirebaseAuthUserCollisionException){
                    exception = "User already exist"
                }catch (e: Exception){
                    exception = "Error with register the user: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(this, exception , Toast.LENGTH_SHORT).show()
            }
        })
    }

}