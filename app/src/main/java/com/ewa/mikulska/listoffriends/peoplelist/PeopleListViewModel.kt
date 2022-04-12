package com.ewa.mikulska.listoffriends.peoplelist

import androidx.lifecycle.LiveData
import com.ewa.mikulska.listoffriends.data.Person
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.ewa.mikulska.listoffriends.LiveEvent
import com.ewa.mikulska.listoffriends.R
import com.ewa.mikulska.listoffriends.repository.PeopleRepository
import kotlinx.coroutines.launch

class PeopleListViewModel(
    private val peopleRepository: PeopleRepository
) : ViewModel() {
    val peopleList = peopleRepository.getPeople()
    private val mutableNaviagtionEvent = LiveEvent<NavDirections>()
    val navigationEvent: LiveData<NavDirections> = mutableNaviagtionEvent
    private val mutableEvent = LiveEvent<MyEvent>()
    val event: LiveData<MyEvent> = mutableEvent

    fun searchDatabase(searchString: String): LiveData<List<Person>> {
        return peopleRepository.searchDatabase(searchString)
    }

    fun removeFromFriends(person: Person) {
        viewModelScope.launch {
            try {
                peopleRepository.changeFriend(person, false)
            } catch (
                e: Exception
            ) {
                mutableEvent.value = MyEvent.ErrorFriend(R.string.toast_error, person)
            }
        }
    }

    fun addToFriends(person: Person) {
        viewModelScope.launch {
            try {
                peopleRepository.changeFriend(person, true)
            } catch (
                e: Exception
            ) {
                mutableEvent.value = MyEvent.ErrorFriend(R.string.toast_error, person)
            }
        }
    }

    fun goToAddFriendDialog() {
        mutableNaviagtionEvent.value =
            PeopleListFragmentDirections.actionPeopleListFragmentToAddPersonDialog()
    }

    fun goToPersonDetails(person: Person) {
        mutableNaviagtionEvent.value =
            PeopleListFragmentDirections.actionPeopleListFragmentToPeopleDetailsFragment(person.id)
    }

    sealed class MyEvent {
        class Error(val message: Int) : MyEvent()
        class ErrorFriend(val message: Int, val personPosition: Person) : MyEvent()
    }
}