package com.example.organize.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.organize.R
import com.example.organize.config.ConfigFirebase
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {
    private lateinit var authentication: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Slides
        isButtonBackVisible = false ;
        isButtonNextVisible = false ;


        addSlide(FragmentSlide.Builder().background(android.R.color.white).fragment(
            R.layout.intro_1
        ).build());

        addSlide(FragmentSlide.Builder().background(android.R.color.white).fragment(
            R.layout.intro_2
        ).build());

        addSlide(FragmentSlide.Builder().background(android.R.color.white).fragment(
            R.layout.intro_3
        ).build());

        addSlide(FragmentSlide.Builder().background(android.R.color.white).fragment(
            R.layout.intro_4
        ).build());

        addSlide(FragmentSlide.Builder().background(android.R.color.white).fragment(
            R.layout.register
        ).canGoForward(false).build());

    }

    override fun onStart() {
        super.onStart()
        alreadyLoggedIn()
    }

     fun btLogIn (view :View) {
         startActivity(Intent(this , LoginActivity::class.java ));
     }
    fun btRegister(view :View){

        startActivity(Intent(this , RegisterActivity::class.java ));

    }

    fun alreadyLoggedIn(){
        authentication = ConfigFirebase.getFirebaseAuthentication()
        if (authentication.currentUser !=null){
            startActivity(Intent(this , MainScreenActivity::class.java))
        }
    }

}