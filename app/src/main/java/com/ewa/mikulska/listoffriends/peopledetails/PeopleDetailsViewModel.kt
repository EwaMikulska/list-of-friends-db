package com.ewa.mikulska.listoffriends.peopledetails

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.ewa.mikulska.listoffriends.LiveEvent
import com.ewa.mikulska.listoffriends.R
import com.ewa.mikulska.listoffriends.repository.PeopleRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class PeopleDetailsViewModel  (
    private val peopleRepository: PeopleRepository
) : ViewModel() {
    private val mutablePersonId = MutableLiveData<UUID>()
    val person = mutablePersonId.switchMap { id -> peopleRepository.getPerson(id) }
    private val mutableNaviagtionEvent = LiveEvent<NavDirections>()
    val navigationEvent: LiveData<NavDirections> = mutableNaviagtionEvent
    private val mutableEvent = LiveEvent<MyEvent>()
    val event: LiveData<MyEvent> = mutableEvent

    fun getDetails(id: UUID) {
        mutablePersonId.postValue(id)
    }

    fun addDescription (personId: UUID, description: String) {
        viewModelScope.launch {
            peopleRepository.changeDescription(personId, description)
        }
    }

    fun removeFriend() {
        val person = person.value ?: return
        viewModelScope.launch {
            try {
                peopleRepository.removePerson(person)
                mutableNaviagtionEvent.value =
                    PeopleDetailsFragmentDirections.actionPeopleDetailsFragmentToPeopleListFragment()
            } catch (
                e: Exception
            ) {
                mutableEvent.value = MyEvent.Error(R.string.toast_error)
            }
        }
    }

    sealed class MyEvent {
        data class Error(val message: Int) : MyEvent()
    }
}