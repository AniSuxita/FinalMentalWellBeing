package com.example.mentalwellbeing

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.mentalwellbeing.databinding.AddEditTaskForchecklistBinding
import com.example.mentalwellbeing.databinding.FragmentChangePasswordBinding
import com.example.mentalwellbeing.databinding.FragmentChecklistBinding
import com.example.mentalwellbeing.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var uri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        binding = FragmentProfileBinding.bind(view)


     val storageRef = Firebase.storage.reference


        var useruid = FirebaseAuth.getInstance().currentUser?.uid


        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(useruid.toString()).get().addOnSuccessListener {
            val firstname = it.child("ufirstname").value
            val lastname = it.child("ulastname").value
            val username = it.child("username").value

            binding.textviewName.text = firstname.toString()
            binding.textviewLastname.text = lastname.toString()
            binding.textviewUsername.text = username.toString()
        }

        binding.logout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("log out")
                .setMessage("are you sure?")
                .setNegativeButton("no") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("yes") { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    FirebaseAuth.getInstance().signOut()
                }
                .show()
        }

        storageRef.child("images/$useruid.jpg").downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.profilepic)
        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.profilepic.setImageURI(it)

                    binding.profilepic.isDrawingCacheEnabled = true
                    binding.profilepic.buildDrawingCache()

                    val bitmap = (binding.profilepic.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                storageRef.child("images/$useruid.jpg").putBytes(data)

                database.child(useruid.toString()).child("profilepic").setValue("$useruid.jpg")



            }
        )

        binding.addphoto.setOnClickListener{
            galleryImage.launch("image/*")
        }

        binding.hotline.setOnCheckedChangeListener { buttonView, isChecked ->
            val sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.apply { putBoolean("CheckboxStatus",isChecked) }?.apply()
        }


        binding.chengebtn.setOnClickListener{
            showDialog(ProfileDialogFragment())
        }



        return view

    }

    fun showDialog(dialogFragment: DialogFragment) {
        val fragmentManager = parentFragmentManager
        val newFragment = dialogFragment

        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()

    }


    override fun onStart() {
        super.onStart()
        val sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val checkstatus = sharedPref!!.getBoolean("CheckboxStatus", false)

        if (checkstatus){
            binding.hotline.isChecked = true
            binding.hotlinenum.text = "psychological counseling services: 08 00 00 00 88"
            binding.number.text = "Psycho-social service center hotline: 2200220 (3025)"

        }

    }

}

class ProfileDialogFragment : DialogFragment() {
    private lateinit var binding:FragmentChangePasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser

        binding.chengebtnn.setOnClickListener{
            when{
                binding.currentpas.text.toString().isEmpty() -> Toast.makeText(requireActivity(),"Please enter current password", Toast.LENGTH_SHORT).show()
                binding.newpas.text.toString().isEmpty() -> Toast.makeText(requireActivity(),"Please enter new password", Toast.LENGTH_SHORT).show()
                binding.newpasrepeat.text.toString().isEmpty() -> Toast.makeText(requireActivity(),"Please repeat new password", Toast.LENGTH_SHORT).show()
                binding.newpas.text.toString().length < 8 -> Toast.makeText(requireActivity(), "Your password is not long enough", Toast.LENGTH_SHORT).show()
                !binding.newpas.text.toString().any { it.isDigit() } -> Toast.makeText(requireActivity(), "Your password must contain digits", Toast.LENGTH_SHORT).show()
                !binding.newpas.text.toString().any{it.isLetter()} -> Toast.makeText(requireActivity(), "Your password must contain letters", Toast.LENGTH_SHORT).show()
                !binding.newpas.text.toString().any { it.isUpperCase() } -> Toast.makeText(requireActivity(), "Your password must contain uppercase letter", Toast.LENGTH_SHORT).show()
                binding.newpas.text.toString() != binding.newpasrepeat.text.toString() -> Toast.makeText(requireActivity(),"Please repeat password correctly", Toast.LENGTH_SHORT).show()
                else ->if (user!=null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, binding.currentpas.text.toString())

                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(requireActivity(), "Re-Authentication success", Toast.LENGTH_SHORT).show()

                                user?.updatePassword(binding.newpas.text.toString())
                                    ?.addOnCompleteListener {
                                        if (it.isSuccessful){
                                            Toast.makeText(requireActivity(), "You successfuly changed password", Toast.LENGTH_SHORT).show()
                                            auth.signOut()

                                        } else {
                                            Toast.makeText(requireActivity(), "Password change failed !", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                            } else {
                                Toast.makeText(requireActivity(), "Re-Authentication failed !", Toast.LENGTH_SHORT).show()
                            }
                        }

                }else{

                }
            }
            dismiss()
        }
    }

}

