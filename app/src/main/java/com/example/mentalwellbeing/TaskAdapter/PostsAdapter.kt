package com.example.mentalwellbeing.TaskAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mentalwellbeing.R
import com.example.mentalwellbeing.data.Posts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class PostsAdapter(private val postArray: ArrayList<Posts>): RecyclerView.Adapter<PostsAdapter.ViewHolder> () {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val posttxt: TextView = view.findViewById(R.id.posttxt)
        val postusername: TextView = view.findViewById(R.id.postusername)
        val postpic: CircleImageView = view.findViewById(R.id.postprofilepic)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postitem,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.posttxt.text = postArray[position].post
        holder.postusername.text = postArray[position].user
        Firebase.storage.reference.child(postArray[position].profpic).downloadUrl.addOnSuccessListener {
            Glide.with(holder.postpic.context).load(it).into(holder.postpic)
        }
    }

    override fun getItemCount(): Int = postArray.size


}