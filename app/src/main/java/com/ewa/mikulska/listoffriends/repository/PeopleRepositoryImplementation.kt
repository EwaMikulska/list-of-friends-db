package com.ewa.mikulska.listoffriends.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ewa.mikulska.listoffriends.data.Person
import com.ewa.mikulska.listoffriends.sources.PeopleDataSource
import java.util.*

class PeopleRepositoryImplementation (

    private val peopleDataSource: PeopleDataSource

        ): PeopleRepository {

    override fun getPeople(): LiveData<List<Person>> {
        return peopleDataSource.getPeople()
    }

    override fun getPerson(id: UUID): LiveData<Person?> {
        return peopleDataSource.getPeople().map { it.firstOrNull { p -> p.id == id } }
    }

    override fun searchDatabase(searchString: String): LiveData<List<Person>> {
        return peopleDataSource.searchDatabase(searchString)
    }

    override suspend fun addPerson(person: Person) {
        peopleDataSource.addPerson(person)
    }

    override suspend fun changeFriend(person: Person, friend: Boolean){
        peopleDataSource.changeFriend(person, friend)
    }

    override suspend fun removePerson(person: Person) {
        peopleDataSource.removePerson(person)
    }

    override suspend fun changeDescription(personId: UUID, description: String) {
        peopleDataSource.changeDescription(personId, description)
    }
}