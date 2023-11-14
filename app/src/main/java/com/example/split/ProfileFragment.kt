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

    private lateinit var pasteTokenField: EditText
    private lateinit var pasteTokenBtn: Button
    private lateinit var addToken: Button

    private lateinit var ipAddressText: TextView
    private lateinit var ipAddressField: EditText
    private lateinit var saveIPAddressBtn: Button

    private lateinit var identifierText: TextView
    private lateinit var identifierField: EditText
    private lateinit var saveIdentifierBtn: Button

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

        identifierText = view.findViewById(R.id.identifierText)
        identifierField = view.findViewById(R.id.identifierField)
        saveIdentifierBtn = view.findViewById(R.id.saveIdentifierBtn)

        ipAddressText = view.findViewById(R.id.ipAddressText)
        ipAddressField = view.findViewById(R.id.ipAddressField)
        saveIPAddressBtn = view.findViewById(R.id.saveIPAddress)

        pasteTokenField = view.findViewById(R.id.pasteTokenField)
        pasteTokenBtn = view.findViewById(R.id.pasteTokenBtn)
        addToken = view.findViewById(R.id.addToken)

        token = view.findViewById(R.id.token)

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!

        database = DBHelper(activity)

        val id = sharedPref.getString("id", null).toString()

        val tokenDb = database.getToken(id)

        val ipAddressDB = database.getIPAddress(id)

        val identifierDb = database.getIdentifier(id)

        if(identifierDb != null){
            identifierText.text = identifierDb
        }

        if(ipAddressDB != null){
            ipAddressText.text = ipAddressDB
        }

        saveIPAddressBtn.setOnClickListener {
            if(ipAddressField.text.isEmpty()){
                Toast.makeText(activity, "Empty IP Address", Toast.LENGTH_SHORT).show()
            }else{
                database.inputIPAddress(id, ipAddressField.text.toString())

                val updateIPAddress = database.getIPAddress(id)

                if(updateIPAddress != null){
                    ipAddressText.text = updateIPAddress
                    ipAddressField.setText("")
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        saveIdentifierBtn.setOnClickListener {
            if(identifierField.text.isEmpty()){
                Toast.makeText(activity, "Empty Identifier", Toast.LENGTH_SHORT).show()
            }else{
                database.inputIdentifier(id, identifierField.text.toString())

                val updateIdentifier = database.getIdentifier(id)

                if(updateIdentifier != null){
                    identifierText.text = updateIdentifier
                    identifierField.setText("")
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
                }

            }
        }

        if(tokenDb != null && tokenDb.length > 4){
            token.text = "******${tokenDb.substring(tokenDb.length - 4)}"
        }else if(tokenDb != null && tokenDb.length < 4){
            token.text = "******${tokenDb}"
        }else{
            token.text = "NO TOKEN"
        }

        pasteTokenBtn.setOnClickListener {
            val tokenString = pasteTokenField.text
            
            if(tokenString.isEmpty()){
                Toast.makeText(activity, "Empty Token", Toast.LENGTH_SHORT).show()
            }else{
                database.inputToken(id, tokenString.toString())

                val updateTokenDb = database.getToken(id)

                if(updateTokenDb!!.length > 10){
                    token.text = "******${updateTokenDb.substring(updateTokenDb.toString().length - 4)}"
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
                    pasteTokenField.setText("")
                }else{
                    token.text = "******${updateTokenDb}"
                    pasteTokenField.setText("")
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val qrCode = registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                database.inputToken(id, result.contents.toString())
                token.text = "******${result.contents.toString().substring(result.contents.toString().length - 4)}"
                Toast.makeText(activity, "Scanned" + result.contents, Toast.LENGTH_LONG).show()
                pasteTokenField.setText("")
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
                    Toast.makeText(activity, "Saved!!", Toast.LENGTH_SHORT).show()
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