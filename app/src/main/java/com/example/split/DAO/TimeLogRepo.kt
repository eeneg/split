package com.example.split.DAO

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TimeLogRepo(private val timeLogDAO: TimeLogDAO) {

    val allLogs: Flow<List<TimeLog>> = timeLogDAO.getTimeLog()

    @WorkerThread
    suspend fun insert(bib: String, time: String) {
        timeLogDAO.insert(bib, time)
    }

    @WorkerThread
    fun update(time: String, id: Int) {
        timeLogDAO.updateTimeLog(time, id)
    }

    @WorkerThread
    fun delete(id: Int) {
        timeLogDAO.deleteTimeLog(id)
    }

}
