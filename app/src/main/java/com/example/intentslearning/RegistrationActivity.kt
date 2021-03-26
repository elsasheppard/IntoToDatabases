package com.example.intentslearning

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // test to see if the username came across

        // to "check the mail", we get the Intent that started this Activity by just using Intent
        // "open the mail" by getting the extra from that Intent
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
        // toast needs activity it's in, message, and length to display
        editText_register_username.setText(username)

        button_register_cancel.setOnClickListener {
            finish()
        }

        button_register_submit.setOnClickListener {
            when {
                editText_register_username.text.toString().length < 3 -> {
                    Toast.makeText(this, "username too short", Toast.LENGTH_SHORT).show()
                }
                editText_register_password.text.toString() != editText_register_passwordConfirm.text.toString() -> {
                    Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    registerUser()
                }
            }
        }
    }

    private fun registerUser() {
        // TODO: data validation to make sure these fields aren't empty (some already above)

        // sets up the backendless user
        val user = BackendlessUser()
        user.setProperty("email", editText_register_email.text.toString())
        user.setProperty("name", editText_register_name.text.toString())
        user.setProperty("username", editText_register_username.text.toString())
        user.password = editText_register_password.text.toString()

        Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(registeredUser: BackendlessUser?) {
                // user has been registered and now can login
                Toast.makeText(this@RegistrationActivity, "Registration successful :)", Toast.LENGTH_SHORT).show()
                val intent = Intent().apply {
                    putExtra(EXTRA_USERNAME, editText_register_username.text.toString())
                    putExtra(EXTRA_PASSWORD, editText_register_password.text.toString())
                }
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(this@RegistrationActivity, fault.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}