package com.example.split.DAO

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TimeLogRepo(private val timeLogDAO: TimeLogDAO) {


    var allLogs: Flow<List<TimeLog>> = timeLogDAO.getTimeLog()

    @WorkerThread
    suspend fun insert(log: TimeLog) {
        try {
            timeLogDAO.insert(log)
        }catch (_: SQLiteConstraintException){

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @WorkerThread
    fun update(time: String, bib: String,id: Int) {
        GlobalScope.launch { timeLogDAO.updateTimeLog(time, bib, id) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @WorkerThread
    fun delete(log: TimeLog) {
        GlobalScope.launch { timeLogDAO.deleteTimeLog(log) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteAll(id: String){
        GlobalScope.launch { timeLogDAO.deleteAll(id) }
    }

}
