package com.example.split.DAO.Event

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Event::class), version = 1, exportSchema = false)
abstract  class EventRoomDB : RoomDatabase(){
    abstract fun eventDao(): EventDao

    private class EvenCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {

                    var eventDao = database.eventDao()

                    val event = Event("12k", "1")

                    eventDao.insert(event)
                }
            }
        }
    }

    companion object{
        @Volatile

        private var INSTANCE: EventRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): EventRoomDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventRoomDB::class.java,
                    "event"
                )
                    .addCallback(EvenCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}