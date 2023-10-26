package com.example.split.Database

import android.annotation.SuppressLint
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

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table 'users' ('id' INTEGER primary key AUTOINCREMENT, 'name' varchar(16), 'username' varchar(16), 'token' text, 'password' varchar(50))")

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
        val p0 = this.readableDatabase
        val res = p0.rawQuery("select * from users where username = ?", arrayOf(username))
        if(res.count<=0){
            res.close()
            return false
        }
        res.close()
        p0.close()
        return true
    }


    fun checkUserExist(username: String, password: String) : Boolean {
        val p0 = this.readableDatabase
        val query = p0.rawQuery("select * from users where username = ? and password = ?", arrayOf(username, password))
        return if(query.count > 0){
            query.close()
            true
        }else{
            query.close()
            false
        }
    }

    fun getAllUsers() : Cursor?{
        val p0 = this.readableDatabase
        val query = p0.rawQuery("select * from users", null)
        query.close()
        p0.close()
        return query
    }

    fun getUserDetails(username: String, password: String) : Cursor? {
        val p0 = this.readableDatabase
        val query = p0.rawQuery("select * from users where username = ? and password = ?", arrayOf(username, password))
        return query
    }

    fun changeName(name: String, id: String) : Boolean {
        val p0 = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("name", name)

        val result = p0.update("users", contentValues, "id=?", arrayOf<String>(id))

        return  result == 1
    }

    fun changeLoginCredentials(id: String, username: String, password: String) : Boolean {
        val p0 = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("username", username)
        contentValues.put("password", password)

        val result = p0.update("users", contentValues, "id=?", arrayOf<String>(id))

        return result == 1
    }

    @SuppressLint("Range")
    fun getToken(id: String): String? {
        val p0 = this.readableDatabase
        val query = p0.rawQuery("select * from users where id = ?", arrayOf(id))
        if(query.moveToFirst()){
            return query.getString(query.getColumnIndex("token"))
        }else{
            return null
        }
    }

    fun inputToken(id: String, token: String): Boolean {
        val p0 = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("token", token)

        val result = p0.update("users", contentValues, "id=?", arrayOf(id))

        return result == 1
    }



}