package com.example.split.DAO

import android.app.Application
import com.example.split.DAO.Event.EventRepo
import com.example.split.DAO.Event.EventRoomDB
import com.example.split.DAO.Participant.ParticipantRepo
import com.example.split.DAO.Participant.ParticipantRoomDB
import com.example.split.DAO.TimeLog.TimeLogRepo
import com.example.split.DAO.TimeLog.TimeLogRoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TimeLogApplication: Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TimeLogRoomDB.getDatabase(this, applicationScope) }
    val repo by lazy { TimeLogRepo(database.timelogDao()) }
    private val eventDB by lazy { EventRoomDB.getDatabase(this, applicationScope) }
    val eventRepo by lazy { EventRepo(eventDB.eventDao()) }
    private val participantDB by lazy { ParticipantRoomDB.getDatabase(this, applicationScope) }
    val participantRepo by lazy { ParticipantRepo(participantDB.participantDao()) }
}