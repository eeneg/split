package com.example.split.DAO.Participant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Participant::class), version = 1, exportSchema = false)
abstract  class ParticipantRoomDB : RoomDatabase(){
    abstract fun participantDao(): ParticipantDao

    private class ParticipantDBCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {

                    val participantDao = database.participantDao()

                    val p = Participant("21001", "asd asd", "1", "2k", "1")

                    participantDao.insert(p)

                }
            }
        }
    }

    companion object{
        @Volatile

        private var INSTANCE: ParticipantRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ParticipantRoomDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParticipantRoomDB::class.java,
                    "participant"
                )
                    .addCallback(ParticipantDBCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}