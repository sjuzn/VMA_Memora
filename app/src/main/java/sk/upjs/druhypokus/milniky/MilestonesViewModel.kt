package sk.upjs.druhypokus.milniky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MilestonesViewModel (private val repository: MilestonesRepository) : ViewModel() {

    val milestones = repository.milestones.asLiveData()

    fun delete(milestone: Milestone) {
        viewModelScope.launch {
            repository.delete(milestone)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun insert(milestone: Milestone) {
        viewModelScope.launch {
            repository.insert(milestone)
        }
    }

    fun replaceAll(milestone: List<Milestone>) {
        viewModelScope.launch {
            repository.replaceAll(milestone)
        }
    }

    fun updateMilestone(milestone: Milestone) {
        viewModelScope.launch {
            repository.update(milestone)
        }
    }

    class MilestoneViewModelFactory(private val repository: MilestonesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MilestonesViewModel::class.java)) {
                return MilestonesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}