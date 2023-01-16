package com.example.exercisetracker

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.exercisetracker.data.model.Exercise
import com.example.exercisetracker.data.model.LoggedInUser
import com.example.exercisetracker.ui.theme.ExerciseTrackerTheme
import com.example.exercisetracker.ui.theme.greenColor
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth


class ExerciseActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        val user = Firebase.auth.currentUser

//        lateinit var exerciseList: List<Exercise>
//        exerciseList = ArrayList<Exercise>()

        if (user == null) {
            val intent = Intent(this@ExerciseActivity, MainActivity::class.java)
            startActivity(intent)
        } else {
            database = Firebase.database.reference
            database.child("users").child(user.uid).child("exercise").get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                if (it.value == null) {
                    database.child("default_exercise").get().addOnSuccessListener { result ->
                        database.child("users").child(user.uid).child("exercise")
                            .setValue(result.value)
                    }.addOnFailureListener {
                        Log.e("firebase", "Error getting data", it)
                    }
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }



            super.onCreate(savedInstanceState)
            setContent {
                ExerciseTrackerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Scaffold(
                            topBar = {
                                topBar(
                                    LocalContext.current
                                )
                            },
                            content = {
                                gridView(
                                    LocalContext.current,
                                    modifier = Modifier.padding(it)
                                )
                            }
                        )
                    }
                }
            }
        }
    }


@Composable
fun topBar(context:Context){
        TopAppBar(backgroundColor = greenColor, title = {
            Text(
                text = "Exercise Select",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        },


            navigationIcon = {
                IconButton(onClick = {
                    auth.signOut()
                    val intent = Intent(this@ExerciseActivity, MainActivity::class.java)
                    startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "Navigation icon")
                }
            }

        )

}

// on below line we are creating grid view function for loading our grid view.
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun gridView(context: Context, modifier: Modifier) {
    // on below line we are creating and initializing our array list
    lateinit var exerciseList: List<Exercise>
    exerciseList = ArrayList<Exercise>()
    exerciseList = exerciseList + Exercise("Dumbbell Curl", R.drawable.ic_dumbell)
    exerciseList = exerciseList + Exercise("Bench Press", R.drawable.ic_dumbell)
    exerciseList = exerciseList + Exercise("Barbell Squat", R.drawable.ic_dumbell)

    // on below line we are adding lazy
    // vertical grid for creating a grid view.
    LazyVerticalGrid(
        // on below line we are setting the
        // column count for our grid view.
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
        columns = GridCells.Fixed(3),
        // on below line we are adding padding
        // from all sides to our grid view.
        modifier = Modifier.padding(30.dp)
    ) {
        // on below line we are displaying our
        // items up to the size of the list.
        items(exerciseList.size) {
            // on below line we are creating a
            // card for each item of our grid view.
            Card(
                // inside our grid view on below line we are
                // adding on click for each item of our grid view.
                onClick = {
                    // inside on click we are displaying the toast message.
                    Toast.makeText(
                        context,
                        exerciseList[it].name + " selected..",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ExerciseActivity, PRActivity::class.java)
                    intent.putExtra("Exercise",exerciseList[it].name)
                    startActivity(intent)
                },

                // on below line we are adding padding from our all sides.
                modifier = Modifier.padding(8.dp),

                // on below line we are adding elevation for the card.
                elevation = 6.dp
            ) {
                // on below line we are creating a column on below sides.
                Column(
                    // on below line we are adding padding
                    // padding for our column and filling the max size.
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp),

                    // on below line we are adding
                    // horizontal alignment for our column
                    horizontalAlignment = Alignment.CenterHorizontally,

                    // on below line we are adding
                    // vertical arrangement for our column
                    verticalArrangement = Arrangement.Center
                ) {
                    // on below line we are creating image for our grid view item.
                    Image(
                        // on below line we are specifying the drawable image for our image.
                        painter = painterResource(id = exerciseList[it].img),

                        // on below line we are specifying
                        // content description for our image
                        contentDescription = "Javascript",

                        // on below line we are setting height
                        // and width for our image.
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                            .padding(5.dp)
                    )

                    // on the below line we are adding a spacer.
                    Spacer(modifier = Modifier.height(9.dp))

                    // on below line we are creating
                    // a text for our grid view item
                    Text(
                        // inside the text on below line we are
                        // setting text as the language name
                        // from our modal class.
                        text = exerciseList[it].name,

                        // on below line we are adding padding
                        // for our text from all sides.
                        modifier = Modifier.padding(4.dp),

                        // on below line we are
                        // adding color for our text
                        color = Color.Black
                    )
                }
            }
        }
    }
}
}
