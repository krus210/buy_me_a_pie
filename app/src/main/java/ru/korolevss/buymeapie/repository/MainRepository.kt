package ru.korolevss.buymeapie.repository

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.korolevss.buymeapie.contract.MainContract
import ru.korolevss.buymeapie.exception.GetPostsException
import ru.korolevss.buymeapie.model.Post

object MainRepository: MainContract.Repository {

    private const val BASE_URL = "https://pivl.github.io/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(API::class.java)

    override suspend fun getPosts(): MutableList<Post> {
        val result = api.getPosts()
        if (result.isSuccessful) {
            Log.d("RETROFIT", "YES")
            return result.body()!!
        } else throw GetPostsException()
    }

}