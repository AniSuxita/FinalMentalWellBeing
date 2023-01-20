package com.example.mentalwellbeing

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.example.mentalwellbeing.data.Userdata
import com.example.mentalwellbeing.databinding.FragmentChecklistBinding
import com.example.mentalwellbeing.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var registerbtn: Button
    private lateinit var emailreg: EditText
    private lateinit var passwordreg: EditText
    private lateinit var passwordrepeatreg: EditText
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var username: EditText
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        auth =  FirebaseAuth.getInstance()
        registerbtn = view.findViewById(R.id.registerbtn)
        emailreg = view.findViewById(R.id.emailreg)
        passwordreg = view.findViewById(R.id.passwordreg)
        passwordrepeatreg = view.findViewById(R.id.repeatpasswordreg)
        firstname = view.findViewById(R.id.firstname)
        lastname = view.findViewById(R.id.lastname)
        username = view.findViewById(R.id.username)
        imageView = view.findViewById(R.id.imageView)

        binding = FragmentRegisterBinding.bind(view)


        registerbtn.setOnClickListener{
            signUpUser()
            saveuserdta()
        }


        return view


    }

    private fun saveuserdta() {
        var useruid = FirebaseAuth.getInstance().currentUser?.uid
        val firstname = binding.firstname.text.toString()
        val lastname = binding.lastname.text.toString()
        val username = binding.username.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = Userdata(firstname,lastname,username)
        database.child(useruid.toString()).setValue(User).addOnSuccessListener {
            binding.firstname.text.clear()
            binding.lastname.text.clear()
            binding.username.text.clear()

        }

    }

    private fun signUpUser() {

            when{
                firstname.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your name", Toast.LENGTH_SHORT).show()
                firstname.text.toString().any{it.isDigit()} -> Toast.makeText(requireActivity(), "Please enter your first name correctly", Toast.LENGTH_SHORT).show()
                lastname.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your last name", Toast.LENGTH_SHORT).show()
                lastname.text.toString().any{it.isDigit()} -> Toast.makeText(requireActivity(), "Please enter your last name correctly", Toast.LENGTH_SHORT).show()
                username.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your username", Toast.LENGTH_SHORT).show()
                emailreg.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your email", Toast.LENGTH_SHORT).show()
                !Patterns.EMAIL_ADDRESS.matcher(emailreg.text.toString()).matches() ->             Toast.makeText(requireActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show()
                passwordreg.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your password", Toast.LENGTH_SHORT).show()
                passwordreg.text.toString().length < 8 -> Toast.makeText(requireActivity(), "Your password is not long enough", Toast.LENGTH_SHORT).show()
                !passwordreg.text.toString().any { it.isDigit() } -> Toast.makeText(requireActivity(), "Your password must contain digits", Toast.LENGTH_SHORT).show()
                !passwordreg.text.toString().any{it.isLetter()} -> Toast.makeText(requireActivity(), "Your password must contain letters", Toast.LENGTH_SHORT).show()
                !passwordreg.text.toString().any { it.isUpperCase() } -> Toast.makeText(requireActivity(), "Your password must contain uppercase letter", Toast.LENGTH_SHORT).show()
                passwordreg.text.toString()!=passwordrepeatreg.text.toString() -> Toast.makeText(requireActivity(), "Please repeat your password correctly", Toast.LENGTH_SHORT).show()
                else -> auth.createUserWithEmailAndPassword(emailreg.text.toString(),passwordreg.text.toString())
                    .addOnCompleteListener(requireActivity()) {task ->
                        if (task.isSuccessful){
                            Toast.makeText(requireActivity(), "Registration successful", Toast.LENGTH_SHORT).show()
                            saveuserdta()
                            emailreg.text.clear()
                            passwordreg.text.clear()
                            passwordrepeatreg.text.clear()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                        } else {
                            Toast.makeText(requireActivity(), "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }



    public override fun onStart() {
        super.onStart()

        val ttb = AnimationUtils.loadAnimation(requireActivity(), R.anim.ttb);
        val bbc = AnimationUtils.loadAnimation(requireActivity(), R.anim.bbc)

        firstname.startAnimation(ttb)
        lastname.startAnimation(ttb)
        username.startAnimation(ttb)
        emailreg.startAnimation(ttb)
        passwordreg.startAnimation(ttb)
        passwordrepeatreg.startAnimation(ttb)
        imageView.startAnimation(bbc)




    }









}


