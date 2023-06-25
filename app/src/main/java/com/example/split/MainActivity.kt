package com.example.split

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.split.Database.DBHelper
import com.example.split.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedPref : SharedPreferences

    private lateinit var db : DBHelper

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.mainToolbar.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.drawerView
        val navController = findNavController(R.id.navHostFragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.writeNFC, R.id.scanNFC
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView : View = navView.getHeaderView(0)
        val nameHeader : TextView = headerView.findViewById(R.id.nameHeader)
        val usernameHeader : TextView = headerView.findViewById(R.id.usernameHeader)

        db = DBHelper(this)

        nameHeader.text = db.getName()
        usernameHeader.text = db.getUsername()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedPref = this.getSharedPreferences("key", Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val intent = Intent(this, MainActivity::class.java)
        when(item.itemId){
            R.id.logoutbtn -> {
                editor.clear()
                editor.apply()
                finish()
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}