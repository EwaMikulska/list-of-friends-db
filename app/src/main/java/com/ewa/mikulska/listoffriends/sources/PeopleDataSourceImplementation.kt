package com.ewa.mikulska.listoffriends.sources

import androidx.lifecycle.LiveData
import com.ewa.mikulska.listoffriends.App
import com.ewa.mikulska.listoffriends.data.Person
import java.util.*

class PeopleDataSourceImplementation (
    private val database: App.AppDatabase
        ): PeopleDataSource {

    override fun getPeople(): LiveData<List<Person>> {
        return database.personDao().getPeople()
    }

    override suspend fun addPerson(person: Person){
        database.personDao().addPerson(person)
    }

    override suspend fun changeFriend(person: Person, isFriend: Boolean){
        database.personDao().changeFriend(person.id, isFriend)
    }

    override suspend fun removePerson(person:Person) {
        database.personDao().removeFriend(person.id)
    }

    override suspend fun changeDescription(personId: UUID, description: String) {
        database.personDao().changeDescription(personId, description)
    }

    override fun getPerson(id: UUID): LiveData<Person?> {
        return database.personDao().getPerson(id)
    }

    override fun searchDatabase(searchString: String): LiveData<List<Person>> {
        return database.personDao().searchDatabase(searchString)
    }
}