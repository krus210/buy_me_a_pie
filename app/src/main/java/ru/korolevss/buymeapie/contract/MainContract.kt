package ru.korolevss.buymeapie.contract

import ru.korolevss.buymeapie.model.Post

interface MainContract {

    interface View {
        suspend fun showText(message:Int)
        suspend fun showDeterminateBar(isShowed: Boolean)
        suspend fun showListFirstTime(list: MutableList<Post>, date: String)
        suspend fun showUpdateList(list: MutableList<Post>, date: String)
        suspend fun showStarClicked(post: Post, position: Int)
        suspend fun showUpdateListAfterRefresh(list: MutableList<Post>, date: String)
        suspend fun showStopRefreshing()
    }

    interface Presenter {
        fun onCreate()
        fun onElectedPostsButtonClicked()
        fun onAllPostsButtonClicked()
        fun onStarButtonClicked(post: Post, position: Int)
        fun onRefreshClicked()
    }

    interface Repository {
        suspend fun getPosts(): MutableList<Post>
    }

    interface DataRepository {
        suspend fun savePosts(listOfPosts: MutableList<Post>)
        suspend fun addElectedPost(post: Post)
        suspend fun getElectedPosts(): MutableList<Post>?
        suspend fun getPosts(): MutableList<Post>?
    }
}