package com.example.intentslearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // similar to what we did in the stopwatch with the bundle
    // to access public constants in other classes, you ave to put them in a companion object
    // without static in kotlin, this is how we make the variable accessible without creating an instance of the object
    companion object {
        val EXTRA_USERNAME = "username"     // to help us remember what the key is
        // put the request code constant here
        val REQUEST_LOGIN_INFO = 1001
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY);

        // make the backendless call to login when they click the login button
        button_login_login.setOnClickListener {
            // extract the username and password from the edittexts
            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()

            // any place in the documentation you see new AsyncCallback<Blah>,
            // the kotlin version is object : AsyncCallback<Blah> {}
            Backendless.UserService.login(
                username,
                password,
                object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        // ?. is the same as doing if(blah != null) { // then do that function or access var }
                        Toast.makeText(
                            this@LoginActivity,
                            "${response?.userId} has logged in",
                            Toast.LENGTH_SHORT
                        ).show()
                        // bring the user to a new activity that's the "home" activity
                        // in this case, the GradeListActivity
                        val gradeListIntent = Intent(this@LoginActivity, GradeListActivity::class.java)
                        startActivity(gradeListIntent)
                        // to close the login screen so it's not there when they click back
                        finish()
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        Toast.makeText(this@LoginActivity, "Something went wrong. Check the logs.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "handleFault: " + fault?.message)
                    }
                }
            )
        }








        // make an onClickListener for the sign up button
        button_login_signup.setOnClickListener {
            // extract the current text in the username box
            val username = editText_login_username.text.toString()

            // create an Intent to that will launch the Registration Activity
            // Intent needs to know where you are coming from, and where you are going
            // FileName::class.java gives you access to the class location for the Intent
            val registrationIntent = Intent(this, RegistrationActivity::class.java).apply {
                // on this Intent, put extra
                // store that username in an "extra" in that Intent
                // another key-value pair
                putExtra(EXTRA_USERNAME, username)
            }

            // store that username in an "extra" in that Intent
            // another key-value pair
            // registrationIntent.putExtra(EXTRA_USERNAME, username)

            // launch the new Activity
            startActivityForResult(registrationIntent, REQUEST_LOGIN_INFO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOGIN_INFO) {
            if (resultCode == RESULT_OK) {
                editText_login_username.setText(data?.getStringExtra(RegistrationActivity.EXTRA_USERNAME))
                editText_login_password.setText(data?.getStringExtra(RegistrationActivity.EXTRA_PASSWORD))
            }
        }
    }

}