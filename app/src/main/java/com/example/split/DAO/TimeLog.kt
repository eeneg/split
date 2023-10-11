package com.example.split.DAO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "racelog", indices = [Index(value = ["bib", "time"], unique = true)])
class TimeLog (

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "bib") var bib: String,
    @ColumnInfo(name = "time")var time: String,

)