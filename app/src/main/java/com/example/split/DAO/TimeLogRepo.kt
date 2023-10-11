package com.example.split.DAO

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

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
//    @WorkerThread
//    fun delete(id: TimeLog) {
//        timeLogDAO.deleteTimeLog(id)
//    }

}
