package com.example.intentslearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // similar to what we did in the stopwatch with the bundle
    // to access public constants in other classes, you ave to put them in a companion object
    companion object {
        val EXTRA_USERNAME = "username"     // to help us remember what the key is
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // make an onClickListener for the sign up button
        button_login_signup.setOnClickListener {
            // extract the current text in the username box
            val username = editText_login_username.text.toString()

            // create an Intent to that will launch the Registration Activity
            // Intent needs to know where you are coming from, and where you are going
            // FileName::class.java gives you access to the class location for the Intent
            val registrationIntent = Intent(this, RegistrationActivity::class.java)

            // store that username in an "extra" in that Intent
            // another key-value pair
            registrationIntent.putExtra(EXTRA_USERNAME, username)

            // launch the new Activity
            startActivity(registrationIntent)
        }
    }
}