package com.example.split

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.split.Database.DBHelper
import com.example.split.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private lateinit var database: DBHelper

    private lateinit var fullname: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var registerBtn: Button


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        fullname = view.findViewById(R.id.name)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirmPassword)
        registerBtn = view.findViewById(R.id.registerBtn)

        database = DBHelper(activity)

        registerBtn.setOnClickListener{

            val fullname = fullname.text.toString()
            val username = username.text.toString()
            val password = password.text.toString()
            val confpass = confirmPassword.text.toString()


            if(TextUtils.isEmpty(fullname)  || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confpass)){
                Toast.makeText(activity, "Invalid Input!", Toast.LENGTH_SHORT).show()
            }else if(password != confpass){
                Toast.makeText(activity, "Password does not match!", Toast.LENGTH_SHORT).show()
            }else if(checkUsername(username)){
                Toast.makeText(activity, "Username already in use!", Toast.LENGTH_SHORT).show()
            }else{
                val insert = createUser(fullname, username, password)
                if(insert){
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }else{
                    Toast.makeText(activity, "Failed to add user!", Toast.LENGTH_SHORT).show()
                }
            }


        }

        return view

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkUsername(username : String): Boolean {
        return database.checkUsername(username)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createUser(name: String, username: String, password: String): Boolean {
        return database.createUser(name, username, password)
    }

}