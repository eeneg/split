package com.example.split.DAO

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TimeLogRepo(private val timeLogDAO: TimeLogDAO) {

    val allLogs: Flow<List<TimeLog>> = timeLogDAO.getTimeLog()

    @WorkerThread
    suspend fun insert(log: TimeLog) {
        try {
            timeLogDAO.insert(log)
        }catch (_: SQLiteConstraintException){
            
        }
    }

//    @WorkerThread
//    fun update(time: TimeLog, id: TimeLog) {
//        timeLogDAO.updateTimeLog(time, id)
//    }
//
    @OptIn(DelicateCoroutinesApi::class)
    @WorkerThread
    fun delete(log: TimeLog) {
        GlobalScope.launch { timeLogDAO.deleteTimeLog(log) }
    }

}
