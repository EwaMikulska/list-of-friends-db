package com.ewa.mikulska.listoffriends.sources

import androidx.lifecycle.LiveData
import com.ewa.mikulska.listoffriends.data.Person
import java.util.*

interface PeopleDataSource {
    fun getPeople(): LiveData<List<Person>>
    fun getPerson(id: UUID): LiveData<Person?>
    suspend fun addPerson(person: Person)
    suspend fun changeFriend(person: Person, isFriend: Boolean)
    suspend fun removePerson(person: Person)
    suspend fun changeDescription(personId: UUID, description: String)
}