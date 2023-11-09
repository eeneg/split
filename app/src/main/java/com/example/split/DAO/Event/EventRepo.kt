package com.example.split.DAO.Event

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventRepo(private val eventDao: EventDao) {

    var allEvent: Flow<List<Event>> = eventDao.getEvent()

    @WorkerThread
    suspend fun insert(log: Event) {
        try {
            eventDao.insert(log)
        }catch (_: SQLiteConstraintException){

        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteAll(id: String){
        GlobalScope.launch { eventDao.deleteAll(id) }
    }

}
