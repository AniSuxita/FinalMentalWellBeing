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
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private lateinit var reset: TextView
    private lateinit var register: TextView
    private lateinit var login: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var resettxt: TextView
    private lateinit var registertxt : TextView

    //login activity
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        reset = view.findViewById(R.id.resettxt)
        register = view.findViewById(R.id.registertxt)
        login = view.findViewById(R.id.loginbtn)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        resettxt = view.findViewById(R.id.resettxt)
        registertxt = view.findViewById(R.id.registertxt)
        

        reset.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        register.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        login.setOnClickListener{
            loginactivity()
        }

        auth = FirebaseAuth.getInstance()

        return view
    }


    private fun loginactivity() {
        when{
            email.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your email", Toast.LENGTH_SHORT).show()
            password.text.toString().isEmpty() -> Toast.makeText(requireActivity(), "Please enter your password", Toast.LENGTH_SHORT).show()
            !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() -> Toast.makeText(requireActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show()
            else -> auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener(requireActivity()){ task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        updateUI(user)
                        Intent(requireActivity(),SecondActivity::class.java)

                    } else{
                        Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                }
        }

    }

    public override fun onStart() {
        super.onStart()


        val ttb = AnimationUtils.loadAnimation(requireActivity(), R.anim.ttb);
        val bbc = AnimationUtils.loadAnimation(requireActivity(), R.anim.bbc)

        email.startAnimation(ttb)
        password.startAnimation(ttb)
        resettxt.startAnimation(ttb)
        registertxt.startAnimation(ttb)



        

    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            startActivity(Intent(requireActivity(),SecondActivity::class.java))
        } else {
            Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show()
        }

    }


}