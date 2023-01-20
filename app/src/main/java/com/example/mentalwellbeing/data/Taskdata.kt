package com.example.mentalwellbeing.data

class Taskdata {
    var taskname: String = ""
    var taskdescription: String = ""
    var taskCheck:Boolean = false


    constructor(){}


    constructor(taskName:String,taskdescription:String,taskCheck:Boolean){
        this.taskname = taskName
        this.taskdescription = taskdescription
        this.taskCheck = taskCheck
    }
}
