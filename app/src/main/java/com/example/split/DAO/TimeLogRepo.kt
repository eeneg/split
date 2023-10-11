package com.example.split.DAO

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TimeLogRepo(private val timeLogDAO: TimeLogDAO) {

    val allLogs: Flow<List<TimeLog>> = timeLogDAO.getTimeLog()

    @WorkerThread
    suspend fun insert(log: TimeLog) {
        timeLogDAO.insert(log)
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
