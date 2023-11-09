package com.example.split.DAO.Event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepo: EventRepo): ViewModel() {

    val allEvent: LiveData<List<Event>> = eventRepo.allEvent.asLiveData()

    fun insert(event: Event) = viewModelScope.launch{
        eventRepo.insert(event)
    }

    fun deleteAll(id :String) = viewModelScope.launch{
        eventRepo.deleteAll(id)
    }

}

class EventViewModelFactory(private val repo: EventRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}