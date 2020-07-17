package ru.korolevss.buymeapie.repository

import retrofit2.Response
import retrofit2.http.GET
import ru.korolevss.buymeapie.model.Post

interface API {
    @GET("sample_api/covers/")
    suspend fun getPosts(): Response<MutableList<Post>>
}