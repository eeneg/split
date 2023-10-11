package com.example.split.Databasec

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBRaceLog(context: Context?) : SQLiteOpenHelper(context, "racelog", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
//        p0?.execSQL("create table 'racelog' ('_id' INTEGER PRIMARY KEY AUTOINCREMENT , 'bib' varchar(16), 'time' time)")
//
//        p0?.execSQL("insert into 'racelog' ('bib', 'time') values ('asd', '10:10:10')")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        p0?.execSQL("drop table if exists racelog")
//        onCreate(p0)
    }

    fun insertLog(bib: String, time: String): Boolean{
        val p0 = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("bib", bib)
        contentValues.put("time", time)

        val result = p0.insert("racelog", null, contentValues)
        return result != (-1).toLong()
    }

    fun getAllLogs(): Cursor? {
        val p0 = this.readableDatabase
        val res = p0.rawQuery("select * from racelog order by time DESC", null)
//        res.close()
        return res
    }

}