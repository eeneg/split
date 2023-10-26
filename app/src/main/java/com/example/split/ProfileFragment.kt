package com.example.split

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.split.Database.DBHelper
import com.example.split.databinding.FragmentProfileBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedPref : SharedPreferences

    private lateinit var fullname : EditText
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var confPassword : EditText

    private lateinit var saveNameBtn: Button
    private lateinit var saveAccountBtn: Button

    private lateinit var addToken: Button

    private lateinit var token : TextView

    private lateinit var database : DBHelper

    @SuppressLint("Range", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val view = binding.root

        fullname = view.findViewById(R.id.profile_name)
        username = view.findViewById(R.id.profile_username)
        password = view.findViewById(R.id.profile_password)
        confPassword = view.findViewById(R.id.profile_confirmPassword)

        saveNameBtn = view.findViewById(R.id.saveNameBtn)
        saveAccountBtn = view.findViewById(R.id.saveAccountBtn)

        addToken = view.findViewById(R.id.addToken)

        token = view.findViewById(R.id.token)

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!

        database = DBHelper(activity)

        val id = sharedPref.getString("id", null).toString()

        val tokenDb = database.getToken(id)
        if(tokenDb != null){
            token.text = "******${tokenDb.substring(tokenDb.length - 4)}"
        }else{
            token.text = "NO TOKEN"
        }



        val qrCode = registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                database.inputToken(id, result.contents.toString())
                token.text = "******${result.contents.toString().substring(result.contents.toString().length - 4)}"
                Toast.makeText(activity, "Scanned" + result.contents, Toast.LENGTH_LONG).show()
            }
        }

        addToken.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scan qr code")
            options.setBeepEnabled(true)
            qrCode.launch(options)
        }

        saveNameBtn.setOnClickListener{
            val name = fullname.text.toString()

            if(TextUtils.isEmpty(name)){
                Toast.makeText(activity, "Invalid Input", Toast.LENGTH_SHORT).show()
            }else{
                val result = database.changeName(name, id)

                if(result == true)
                {
                    Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        saveAccountBtn.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()
            val confPassword = confPassword.text.toString()

            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confPassword)){
                Toast.makeText(activity, "Invalid Input", Toast.LENGTH_SHORT).show()
            }else if(password != confPassword){
                Toast.makeText(activity, "Please Reconfirm Password", Toast.LENGTH_SHORT).show()
            }else{
                val result = database.changeLoginCredentials(id, username, password)

                if(result == true){
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    val intent = Intent(activity, MainActivity::class.java)
                    editor.clear()
                    editor.apply()
                    activity?.finish()
                    startActivity(intent)
                }else{
                    Toast.makeText(activity, "Failed!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view

    }


}