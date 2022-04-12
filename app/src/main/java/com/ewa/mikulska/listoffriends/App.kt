package com.ewa.mikulska.listoffriends

import android.app.Application
import com.ewa.mikulska.listoffriends.data.Person
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ewa.mikulska.listoffriends.addingfriend.AddFriendViewModel
import com.ewa.mikulska.listoffriends.data.PersonDao
import com.ewa.mikulska.listoffriends.peopledetails.PeopleDetailsViewModel
import com.ewa.mikulska.listoffriends.peoplelist.PeopleListViewModel
import com.ewa.mikulska.listoffriends.repository.PeopleRepository
import com.ewa.mikulska.listoffriends.repository.PeopleRepositoryImplementation
import com.ewa.mikulska.listoffriends.sources.PeopleDataSource
import com.ewa.mikulska.listoffriends.sources.PeopleDataSourceImplementation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    private val personModules = module {
        single { PeopleDataSourceImplementation(get()) as PeopleDataSource }
        single { PeopleDataSourceImplementation(get()) }
        single { PeopleRepositoryImplementation(get()) as PeopleRepository }
        viewModel { PeopleListViewModel(get()) }
        viewModel { AddFriendViewModel(get()) }
        viewModel { PeopleDetailsViewModel(get()) }

        single {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() // change that later
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(personModules)
        }
    }

    @TypeConverters(DateTimeConverter::class, BitmapConverter::class)
    @Database(entities = [Person::class], version = 11)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun personDao(): PersonDao
    }
}
