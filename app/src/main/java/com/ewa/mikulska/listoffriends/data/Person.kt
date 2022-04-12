package com.ewa.mikulska.listoffriends.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.joda.time.LocalDate
import java.io.Serializable
import java.util.*

@Dao
interface PersonDao {

    @Query("SELECT * FROM Person")
    fun getPeople(): LiveData<List<Person>>

    @Insert
    fun addPerson(person: Person)

    @Query("SELECT * FROM Person WHERE id = :id")
    fun getPerson(id: UUID): LiveData<Person?>

    @Query("UPDATE Person SET isFriend = :isFriend WHERE id = :id")
    fun changeFriend(id: UUID, isFriend: Boolean)

    @Query("DELETE FROM Person WHERE id = :id")
    fun removeFriend(id: UUID)

    @Query("UPDATE Person SET description = :description WHERE id = :id")
    fun changeDescription(id: UUID, description: String)
}

@Entity
data class Person(
    @PrimaryKey
    val id: UUID,
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val isFriend: Boolean,
    val image: ByteArray?,
    val description: String?
) : Serializable




