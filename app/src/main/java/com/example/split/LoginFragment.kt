package com.example.split

import android.content.Context
import android.content.SharedPreferences
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.split.Database.DBHelper
import com.example.split.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var username : EditText
    private lateinit var password : EditText

    private lateinit var loginbtn : Button
    private lateinit var regbtn : Button

    private lateinit var database: DBHelper

    private lateinit var sharedPref : SharedPreferences

    private var sharedPrefUsername = ""
    private var sharedPrefPassword = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

            _binding = FragmentLoginBinding.inflate(inflater, container, false)
            val view = binding.root

            username = view.findViewById(R.id.username)
            password = view.findViewById(R.id.password)

            loginbtn = view.findViewById(R.id.loginbtn)
            regbtn = view.findViewById(R.id.registerBtn)

            database = DBHelper(activity)

            sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!
            sharedPrefUsername = sharedPref.getString("username", "").toString()
            sharedPrefPassword = sharedPref.getString("password", "").toString()


            loginbtn.setOnClickListener {

                val username = username.text.toString()
                val password = password.text.toString()

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(activity, "Invalid Input", Toast.LENGTH_SHORT).show()
                }else if(database.checkUserExist(username, password)){

                    val editor: SharedPreferences.Editor = sharedPref.edit()

                    editor.putString("username", username)
                    editor.putString("password", password)

                    editor.apply()


                    view.findNavController().navigate(R.id.scanNFC)

                }else{
                    Toast.makeText(activity, "Username or Password does not exist", Toast.LENGTH_SHORT).show()
                }

            }

            regbtn.setOnClickListener{
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!


        if(sharedPref.getString("username", null) != null){
            navController.popBackStack()
            navController.navigate(R.id.scanNFC)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
