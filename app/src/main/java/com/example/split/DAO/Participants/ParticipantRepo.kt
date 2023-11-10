package com.example.split.DAO.Participant

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ParticipantRepo(private val participantDao: ParticipantDao) {

    var allParticipant: Flow<List<Participant>> = participantDao.getParticipant()

    @WorkerThread
    suspend fun insert(log: Participant) {
        try {
            participantDao.insert(log)
        }catch (_: SQLiteConstraintException){

        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteAll(id: String){
        GlobalScope.launch { participantDao.deleteAll(id) }
    }

}
