package com.example.exercisetracker




import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercisetracker.data.model.PR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference

class PRActivity : ComponentActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var PRArrayList : ArrayList<PR>


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        if (user ==null)
        {
            val intent = Intent(this@PRActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val ExerciseID=intent.getStringExtra("Exercise")
        if (ExerciseID ==null)
        {
            val intent = Intent(this@PRActivity, ExerciseActivity::class.java)
            startActivity(intent)
        }
        val profileName=intent.getStringExtra("Exercise")
        Log.i("debug", "Context ${profileName}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pr)

        userRecyclerview = findViewById(R.id.userList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        PRArrayList = arrayListOf<PR>()
        getUserData()

        var mButtonAdd = findViewById<Button>(R.id.buttonAdd)
        mButtonAdd.setOnClickListener(View.OnClickListener(){
            Log.e("debug", "TEST:")
            onClick(it)
        })

    }

    public fun onClick(v:View){
        val ExerciseID=intent.getStringExtra("Exercise")
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        fun randomUUID() = UUID.randomUUID().toString()
        val uuid=randomUUID()
        Log.e("debug", "UUID: ${uuid}")
        if(ExerciseID != null && user != null) {
            //Go to new screen to enter new details

            val pr = PR("01/02/2002", "200")
            database.child("exercise_pr").child(ExerciseID).child(user.uid).child(randomUUID()).setValue(pr)
        }
    }

    private fun getUserData() {
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        val ExerciseID=intent.getStringExtra("Exercise")
        database = Firebase.database.reference
        if(ExerciseID!=null && user!=null) {
            Log.e("debug", "Error getting data ${user.uid}")
            dbref=database.child("exercise_pr").child(ExerciseID).child(user.uid)

        }

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("debug", "Snap ${snapshot.value}")
                PRArrayList.clear()
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val pr = userSnapshot.getValue(PR::class.java)
                        PRArrayList.add(pr!!)

                    }

                    userRecyclerview.adapter = MyAdapter(PRArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}