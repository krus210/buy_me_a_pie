package ru.korolevss.buymeapie.repository

import android.content.Context
import androidx.room.Room
import ru.korolevss.buymeapie.contract.MainContract
import ru.korolevss.buymeapie.model.Post

class MainDataRepository(context: Context) : MainContract.DataRepository {

    private val database by lazy { Room.databaseBuilder(
        context, AppDatabase::class.java, "database"
    )
        .fallbackToDestructiveMigration()
        .build() }

    override suspend fun savePosts(listOfPosts: MutableList<Post>) =
        database.getPostDao().insertAll(listOfPosts)

    override suspend fun addElectedPost(post: Post) = database.getPostDao().addElectedPost(post)

    override suspend fun getElectedPosts() = database.getPostDao().getElectedPots()

    override suspend fun getPosts() = database.getPostDao().getAllPots()


}