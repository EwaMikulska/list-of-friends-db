package com.ewa.mikulska.listoffriends.repository

import androidx.lifecycle.LiveData
import com.ewa.mikulska.listoffriends.data.Person
import java.util.*

interface PeopleRepository {
    fun getPeople(): LiveData<List<Person>>
    fun getPerson(id: UUID): LiveData<Person?>
    suspend fun addPerson(person: Person)
    suspend fun changeFriend(person: Person, friend: Boolean)
    suspend fun removePerson(person: Person)
    suspend fun changeDescription(personId: UUID, description: String)
}