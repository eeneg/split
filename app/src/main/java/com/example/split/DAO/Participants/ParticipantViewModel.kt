package com.example.split.DAO.Participant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ParticipantViewModel(private val paticipantRepo: ParticipantRepo): ViewModel() {

    val allParticipant: LiveData<List<Participant>> = paticipantRepo.allParticipant.asLiveData()

    fun insert(participant: Participant) = viewModelScope.launch{
        paticipantRepo.insert(participant)
    }

    fun deleteAll(id :String) = viewModelScope.launch{
        paticipantRepo.deleteAll(id)
    }

}

class ParticipantViewModelFactory(private val repo: ParticipantRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticipantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParticipantViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}