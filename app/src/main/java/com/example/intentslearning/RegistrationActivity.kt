package com.example.intentslearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // test to see if the username came across

        // to "check the mail", we get the Intent that started this Activity by just using Intent
        // "open the mail" by getting the extra from that Intent
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
        Toast.makeText(this, username , Toast.LENGTH_SHORT).show()
    }
}