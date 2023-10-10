package com.example.split.DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TimeLogViewModel(private val repo: TimeLogRepo): ViewModel() {

    val allLogs: LiveData<List<TimeLog>> = repo.allLogs.asLiveData()

    fun insert(bib: String, time: String) = viewModelScope.launch{
        repo.insert(bib, time)
    }

    fun update(time: String, id: Int) = viewModelScope.launch {
        repo.update(time, id)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repo.delete(id)
    }

}

class TimeLogViewModelFactory(private val repo: TimeLogRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeLogViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}