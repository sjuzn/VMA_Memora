package sk.upjs.druhypokus.moments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MomentTagViewModel(private val repository: MomentTagRepository) : ViewModel() {

    val allMoments = repository.allMoments.asLiveData()
    val allTags = repository.allTags.asLiveData()

    fun getTagSMomentami(tag: Tag): LiveData<List<TagWithMoments>> {
        return repository.getTagSMomentami(tag).asLiveData()
    }

    fun getMomentSTagmi(moment: Moment): LiveData<List<MomentWithTags>> {
        return repository.getMomentSTagmi(moment).asLiveData()
    }

    fun insertMoment(moment: Moment) {
        viewModelScope.launch {
            repository.insertMoment(moment)
        }
    }

    fun insertTag(tag: Tag) {
        viewModelScope.launch {
            repository.insertTag(tag)
        }
    }

    fun insertMomentTagCrossRef(crossRef: MomentTagCrossRef) {
        viewModelScope.launch {
            repository.insertMomentTagCrossRef(crossRef)
        }
    }

    fun deleteMoment(moment: Moment) {
        viewModelScope.launch {
            repository.deleteMoment(moment)
        }
    }

    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            repository.deleteTag(tag)
        }
    }

    fun deleteMomentTagCrossRefs(tag: Tag, moment: Moment) {
        viewModelScope.launch {
            repository.deleteMomentTagCrossRefs(tag, moment)
        }
    }

    class MomentTagViewModelFactory(private val repository: MomentTagRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MomentTagViewModel::class.java)) {
                return MomentTagViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
