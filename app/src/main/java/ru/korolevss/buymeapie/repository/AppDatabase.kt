package ru.korolevss.buymeapie.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.korolevss.buymeapie.model.Post

@Database(entities = [Post::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPostDao(): PostDao
}