package com.example.mentalwellbeing.data

class Posts {
    var user: String = ""
    var profpic: String = ""
    var post: String = ""
    var likes: Int = 0

    constructor(){}

    constructor(user:String,profpic:String,post:String,likes:Int){
        this.user = user
        this.profpic = profpic
        this.post = post
        this.likes = likes
    }
}