package com.example.intentslearning

// providing default values for each meets the requirement of having a
// public, no argument constructor b/c now this object could be make without arguments:
// val grade = Grade()
data class Grade (
    var assignment : String = "Assignment",
    var enjoyedAssignment : Boolean = true,
    var letterGradeValue : Int = 4,
    var percentage : Double = 1.0,
    var studentName : String = "Default Student",
    var subject : String = "Default Subject",
    var objectId : String = "",
    var ownerId : String = ""
        )
{

}