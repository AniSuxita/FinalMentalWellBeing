package com.example.mentalwellbeing.TaskAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mentalwellbeing.R
import com.example.mentalwellbeing.data.Taskdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TaskAdapter(val c:Context,val useruid:String,val tasklist:ArrayList<Taskdata>):RecyclerView.Adapter<TaskAdapter.TaskviewHolder>() {

    inner class TaskviewHolder(val v: View):RecyclerView.ViewHolder(v){
        val tasknametxt = v.findViewById<TextView>(R.id.textview_name)
        val chechbox = v.findViewById<CheckBox>(R.id.checkboxcompleted)
        val taskdel = v.findViewById<ImageView>(R.id.taskdel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_task,parent,false)
        return TaskviewHolder(v)
    }

    override fun onBindViewHolder(holder: TaskviewHolder, position: Int) {
        val newlist = tasklist[position]
        holder.tasknametxt.text = newlist.taskname
        holder.chechbox.setOnCheckedChangeListener { compoundButton, b ->
            var isChecked = holder.chechbox.isChecked
            FirebaseDatabase.getInstance().getReference().child("tasks")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child(newlist.taskname)
                .child("taskCheck").setValue(isChecked)

        }

        holder.taskdel.setOnClickListener{

            var name = holder.tasknametxt.text.toString()

            FirebaseDatabase.getInstance().getReference().child("tasks").child(useruid).child(name).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(c, "Error", Toast.LENGTH_SHORT).show()
                }
        }



    }

    override fun getItemCount(): Int {
        return tasklist.size
    }
}