package com.example.mentalwellbeing.data

class Userdata {
    var ufirstname: String = ""
    var ulastname: String = ""
    var username: String = ""

    constructor(){}

    constructor(ufirstname:String,ulastname:String,username:String){
        this.ufirstname = ufirstname
        this.ulastname = ulastname
        this.username = username
    }

}