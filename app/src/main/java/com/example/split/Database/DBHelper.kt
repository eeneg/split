package com.example.split.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class DBHelper(
    context: Context?,
) : SQLiteOpenHelper(context, "users", null, 1) {

    var user_name = ""
    var username_text = ""

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table 'users' ('id' INTEGER primary key AUTOINCREMENT, 'name' varchar(16), 'username' varchar(16), 'password' varchar(50))")

        p0?.execSQL("insert into users (name, username, password) values ('administrator', 'admin', 'root')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists users")
        onCreate(p0)
    }

    fun createUser(name: String, username: String, password: String) : Boolean {
        val p0 = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("name", name)
        contentValues.put("username", username)
        contentValues.put("password", password)
        val result = p0.insert("users", null, contentValues)
        return result != (-1).toLong()
    }

    fun checkUsername(username: String) : Boolean {
        val db = this.readableDatabase
        val res = db.rawQuery("select * from users where username = ?", arrayOf(username))
        if(res.count<=0){
            res.close()
            return false
        }
        res.close()
        db.close()
        return true
    }


    fun checkUserExist(username: String, password: String) : Boolean {
        val db = this.readableDatabase
        val query = db.rawQuery("select * from users where username = ? and password = ?", arrayOf(username, password))
        this.setCurrentUser(username, password)
        return query.count > 0
    }

    fun getAllUsers() : Cursor?{
        val db = this.readableDatabase
        val query = db.rawQuery("select * from users", null)
        query.close()
        db.close()
        return query
    }

    fun setCurrentUser(username: String, password: String){
        val db = this.readableDatabase
        val query = db.rawQuery("select * from users where username = ? and password = ?", arrayOf(username, password))
        query.moveToFirst()
        user_name = query.getString(1)
        username_text = query.getString(2)

    }

    fun getName(): String {
        return user_name
    }

    fun getUsername(): String{
        return username_text
    }


}