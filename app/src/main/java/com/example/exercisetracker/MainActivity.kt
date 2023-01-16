package com.example.exercisetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        if (user != null) {
            val intent = Intent(this@MainActivity, ExerciseActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)
    }
    public fun gotoLogin(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
    public fun gotoReg(view: View) {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }
}