package sk.upjs.druhypokus.bucketList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BListViewModel  (private val repository: BListRepository) : ViewModel() {
    val bList = repository.bListAll.asLiveData()

    fun insert(bList: BList) {
        viewModelScope.launch {
            repository.insert(bList)
        }
    }

    fun update(bList: BList) {
        viewModelScope.launch {
            repository.update(bList)
        }
    }

    fun delete(bList: BList?) {
        viewModelScope.launch {
            if(bList != null)
            repository.delete(bList)
        }
    }

    class BListViewModelFactory(private val repository: BListRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BListViewModel::class.java)) {
                return BListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}