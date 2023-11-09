package com.example.split.DAO.Event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "event", indices = [Index(value = ["eventName"], unique = true)])
class Event (
    @ColumnInfo(name = "eventName") var eventName: String,
    @ColumnInfo(name = "userId") var userId: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
