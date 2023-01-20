package com.example.mentalwellbeing

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mentalwellbeing.TaskAdapter.PostsAdapter
import com.example.mentalwellbeing.data.Posts
import com.example.mentalwellbeing.databinding.AddEditTaskForchecklistBinding
import com.example.mentalwellbeing.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val postRef = FirebaseDatabase.getInstance().getReference("Posts")

        binding = FragmentHomeBinding.bind(view)
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        var useruid = FirebaseAuth.getInstance().currentUser?.uid

        val postArray = arrayListOf<Posts>()

        postRef.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val postitem = snapshot.getValue(Posts::class.java)
                if (postitem!=null){
                    postArray.add(0,Posts(postitem.user,postitem.profpic,postitem.post,postitem.likes))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }

        })

        binding.postrec.layoutManager = LinearLayoutManager(context)
        binding.postrec.adapter = PostsAdapter(postArray)

        databaseRef.child(useruid.toString()).get().addOnSuccessListener {
            val username = it.child("username").value
            binding.homeusername.text = username.toString()
        }

        val storageRef = Firebase.storage.reference
        storageRef.child("images/$useruid.jpg").downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.profilepichome)
        }

        binding.addpost.setOnClickListener{
            if (!binding.post.text.isEmpty()){
                val post = Posts(binding.homeusername.text.toString(),"images/$useruid.jpg",binding.post.text.toString(),0)
                postRef.push().setValue(post)
            }
        }


        return view
    }



}
