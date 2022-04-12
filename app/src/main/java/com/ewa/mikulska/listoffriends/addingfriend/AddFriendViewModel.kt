package com.ewa.mikulska.listoffriends.addingfriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ewa.mikulska.listoffriends.*
import com.ewa.mikulska.listoffriends.data.Person
import com.ewa.mikulska.listoffriends.repository.PeopleRepository
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import java.util.*

class AddFriendViewModel(
    private val peopleRepository: PeopleRepository
): ViewModel() {
    private val mutableEvent = LiveEvent<MyEvent>()
    val event: LiveData<MyEvent> = mutableEvent

    var isFriend: Boolean = false
    var name = String()
    var surname = String()
    var birthDate = LocalDate()
    var image: ByteArray? = null

    fun addNewFriend() {
        viewModelScope.launch {
            if (name.isBlank() || surname.isBlank()) {
                mutableEvent.value = MyEvent.Error("Missed text")
                return@launch
            }
            try {
                peopleRepository.addPerson(
                    Person(
                        id = UUID.randomUUID(),
                        name = name,
                        surname = surname,
                        birthDate = birthDate,
                        isFriend = isFriend,
                        image = image,
                        description = null
                    )
                )
                mutableEvent.value = MyEvent.DismissDialog
            } catch (
                e: Exception
            ) {
                mutableEvent.value = MyEvent.ErrorToast(R.string.toast_error)
            }
        }
    }

    sealed class MyEvent {
        object DismissDialog : MyEvent()
        data class Error(val message: String) : MyEvent()
        data class ErrorToast(val message: Int) : MyEvent()
    }
}

