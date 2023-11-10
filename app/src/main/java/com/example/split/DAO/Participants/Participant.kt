package com.example.split.DAO.Participant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "participant", indices = [Index(value = ["bib"], unique = true)])
class Participant (
    @ColumnInfo(name = "bib") var bib: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "eventId") var eventId: String,
    @ColumnInfo(name = "eventName") var eventName: String,
    @ColumnInfo(name = "userID") var userID: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
