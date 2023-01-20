package com.example.mentalwellbeing

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentalwellbeing.TaskAdapter.TaskAdapter
import com.example.mentalwellbeing.data.Taskdata
import com.example.mentalwellbeing.databinding.AddEditTaskForchecklistBinding
import com.example.mentalwellbeing.databinding.FragmentChecklistBinding
import com.example.mentalwellbeing.databinding.ItemTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChecklistFragment : Fragment(R.layout.fragment_checklist) {
    private lateinit var binding: FragmentChecklistBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var tasklist: ArrayList<Taskdata>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChecklistBinding.bind(view)
        tasklist = ArrayList()

        var useruid = FirebaseAuth.getInstance().currentUser?.uid

        taskAdapter = TaskAdapter(requireActivity(),useruid.toString(), tasklist)

        binding.recyclerviewTasks.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerviewTasks.adapter = taskAdapter



        FirebaseDatabase.getInstance().getReference()
            .child("tasks").child(useruid.toString()).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    tasklist.clear()
                    for(postsnapshot in snapshot.children){
                        val taskObject = postsnapshot.getValue(Taskdata::class.java)
                        if(taskObject != null){
                            tasklist.add(taskObject)
                            Log.d("kaf","sdhas")
                        }
                    }
                    taskAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    var miki = "miki"
                    var mausi = "mausi"
                    var mikimausi = miki + mausi
                }

            })



        class CheckListDialogFragment:DialogFragment(){
            private lateinit var binding:AddEditTaskForchecklistBinding

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
            ): View {
                binding = AddEditTaskForchecklistBinding.inflate(inflater, container, false)

                return binding.root
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)



                    binding.addbtn.setOnClickListener {
                        val taskname = binding.taskname
                        val taskdescription = binding.taskdescription
                        val name = taskname.text.toString()
                        val description = taskdescription.text.toString()
                        var taskCheck = false


                        var taskObject = Taskdata(name, description, taskCheck)

                        if(binding.taskname.text.toString().isNotEmpty()&&
                            binding.taskdescription.text.toString().isNotEmpty()) {
                            FirebaseDatabase.getInstance().getReference().child("tasks")
                                .child(useruid.toString()).child(name).setValue(taskObject)
                            taskAdapter.notifyDataSetChanged()
                            Toast.makeText(requireActivity(), name, Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            Toast.makeText(requireActivity(), "Please write your tasks", Toast.LENGTH_SHORT).show()
                        }
                    }


            }
        }


        binding.addtaskbtn.setOnClickListener{

            showDialog(CheckListDialogFragment())



        }

    }

    fun showDialog(dialogFragment: DialogFragment) {
        val fragmentManager = parentFragmentManager
        val newFragment = dialogFragment

        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }



}
