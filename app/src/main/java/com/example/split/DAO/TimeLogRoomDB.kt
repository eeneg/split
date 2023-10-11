package com.example.split.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(entities = arrayOf(TimeLog::class), version = 1, exportSchema = false)
abstract  class TimeLogRoomDB : RoomDatabase(){

    abstract fun timelogDao(): TimeLogDAO

    private class TimeLogDBCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var timeLogDAO = database.timelogDao()

                    timeLogDAO.deleteAll()

                    var log = TimeLog("000", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString())
                    timeLogDAO.insert(log)
                }
            }
        }
    }

    companion object{
        @Volatile

        private var INSTANCE: TimeLogRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TimeLogRoomDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeLogRoomDB::class.java,
                    "racelog"
                )
                .addCallback(TimeLogDBCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}