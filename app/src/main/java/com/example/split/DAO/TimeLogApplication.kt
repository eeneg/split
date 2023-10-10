package com.example.split.DAO

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TimeLogApplication: Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TimeLogRoomDB.getDatabase(this, applicationScope) }
    val repo by lazy { TimeLogRepo(database.timelogDao()) }

}