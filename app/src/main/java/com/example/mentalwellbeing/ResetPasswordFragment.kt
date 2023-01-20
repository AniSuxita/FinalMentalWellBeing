package com.example.mentalwellbeing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {


    private lateinit var emailreset : EditText
    private lateinit var resetbtn: Button
    private lateinit var iv3: ImageView

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_reset_password, container, false)

        emailreset = v.findViewById(R.id.emailreset)
        resetbtn = v.findViewById(R.id.resetbtn)
        iv3 = v.findViewById(R.id.imageView3)

        auth = FirebaseAuth.getInstance()

        resetbtn.setOnClickListener {

            if (emailreset.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please enter your email", Toast.LENGTH_SHORT)
                    .show()
            } else {


                auth.sendPasswordResetEmail(emailreset.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireActivity(), "Check your email", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
            }

        }
        // Inflate the layout for this fragment
        return v
    }


    public override fun onStart() {
        super.onStart()

        val ttb = AnimationUtils.loadAnimation(requireActivity(), R.anim.ttb);
        val bbc = AnimationUtils.loadAnimation(requireActivity(), R.anim.bbc)

        emailreset.startAnimation(ttb)
        resetbtn.startAnimation(ttb)
        iv3.startAnimation(ttb)


    }

    }

