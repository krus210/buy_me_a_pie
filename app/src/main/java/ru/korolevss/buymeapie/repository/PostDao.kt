package ru.korolevss.buymeapie.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korolevss.buymeapie.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listOfPots: MutableList<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addElectedPost(post: Post)

    @Query("SELECT * FROM post")
    fun getAllPots(): MutableList<Post>

    @Query("SELECT * FROM post WHERE isElected = 1")
    fun getElectedPots(): MutableList<Post>

}