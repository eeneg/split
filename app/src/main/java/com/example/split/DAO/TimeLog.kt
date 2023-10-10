package com.example.split.DAO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time


@Entity(tableName = "racelog")
class TimeLog {

    @PrimaryKey val id: Int = 0
    @ColumnInfo(name = "bib")
    var bib: String? = null
    @ColumnInfo(name = "time")
    var time: Time? = null

}