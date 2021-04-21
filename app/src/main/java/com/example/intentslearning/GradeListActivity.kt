    package com.example.intentslearning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_grade_list.*


class GradeListActivity : AppCompatActivity() {
    companion object {
        val TAG = "GradeListActivity"
    }

    private lateinit var userId : String
    private var gradesList : List<Grade?>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_list)

        // TODO: make a whole recyclerview layout and stuff for the grades

        // for now, log a list of all the grades
        // this retrieves all of the grade from every user, then filters it so only the current
        // user's is shown, generally not the best approach. better to do it server side
        /*
        Backendless.Data.of(Grade::class.java).find(object : AsyncCallback<List<Grade?>?> {
            override fun handleResponse(foundGrades: List<Grade?>?) {
                // all Grade instances have been found

                // get the current user's objectid
                val userId = Backendless.UserService.CurrentUser().userId

                // make a temporary list for just our matches
                val matchingList = mutableListOf<Grade?>()
                if (foundGrades != null) {
                    for(grade in foundGrades)
                        if(grade?.ownerId == userId) {
                            matchingList.add(grade)
                        }
                }
                // returns a new list that only has items that match the condition in the braces
                val matchingList = foundGrades?.filter {
                    it?.ownerId == userId
                }
                Log.d(TAG, "handleResponse :" + matchingList.toString())

            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: " + fault.message)
            }
        })
        */

        // code below here can't guarantee that the data has been retrieved
        // this is executed right away after the async call goes out, but might be
        // before the async call comes back

        fun readAllUserGrades() {
            // do an advanced data retrieval with a where clause that matches the ownerId to current userId
            // ownerId = 'blah'     but 'blah' is our variable for the userId
            val userId = Backendless.UserService.CurrentUser().userId
            val whereClause = "ownerId = '$userId'"
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.setWhereClause(whereClause)

            Backendless.Data.of(Grade::class.java).find(queryBuilder,
                object : AsyncCallback<List<Grade?>?> {
                    override fun handleResponse(foundGrades: List<Grade?>?) {
                        // the "foundGrade" collection now contains instances of the Grade class.
                        // each instance represents an object stored on the server.
                        gradesList = foundGrades
                        Log.d(TAG, "handleResponse :" + foundGrades.toString())
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Log.d(TAG, "handleFault: " + fault.message)

                    }
                }
            )
        }

        button_gradelist_read.setOnClickListener {
            // mr shorr said this could go before readAllUserGrades but it didnt work
            readAllUserGrades()
        }

        button_gradeList_create.setOnClickListener() {
            createNewGrade()
        }

        button_gradeList_update.setOnClickListener() {
            updateFirstGrade()
        }

        button_gradeList_delete.setOnClickListener() {
            deleteFirstGrade()
        }
    }

    private fun deleteFirstGrade() {
        if(!gradesList.isNullOrEmpty()) {
            // make the backendless call to delete the first item in the list
            // get the first item
            val grade = gradesList?.get(0)
            // make the backendless call
            Backendless.Data.of(Grade::class.java).remove(grade,
                object : AsyncCallback<Long?> {
                    override fun handleResponse(response: Long?) {
                        Log.d(TAG, "handleResponse: item deleted at $response")
                        // Contact has been deleted. The response is the
                        // time in milliseconds when the object was deleted
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be
                        // retrieved with fault.getCode()
                        Log.d(TAG, "handleFault: ${fault.message}")
                    }
                }
            )
        }
    }

    private fun updateFirstGrade() {
        if(!gradesList.isNullOrEmpty()) {
            // get the first item in the list
            val grade = gradesList?.get(0)
            grade?.assignment = "new and improved updated item"

            Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
                override fun handleResponse(response: Grade?) {
                    Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show()
                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d(TAG, "handleFault: ${fault.message}")
                }
            })
        }
    }

    private fun createNewGrade() {
        Log.d(TAG, "createNewGrade: $userId")
        val grade = Grade(
            assignment = "Chapter 5 Multiple Choice",
            studentName = "Alden",
            ownerId = userId
        )
        grade.ownerId = userId
        Log.d(TAG, "createNewGrade: ")
        
        
        // do an advanced data retrieval with a where clause that matches the ownerId to current userId
        Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
            override fun handleResponse(response: Grade?) {
                Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })
    }
}