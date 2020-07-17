package ru.korolevss.buymeapie.presenter

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.korolevss.buymeapie.R
import ru.korolevss.buymeapie.contract.MainContract
import ru.korolevss.buymeapie.exception.GetPostsException
import ru.korolevss.buymeapie.model.Post
import ru.korolevss.buymeapie.repository.MainDataRepository
import ru.korolevss.buymeapie.repository.MainRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainPresenter(
    private val mainView: MainContract.View,
    private val mainRepository: MainContract.Repository = MainRepository,
    private val mainDataRepository: MainContract.DataRepository = MainDataRepository(mainView as Context)
) : MainContract.Presenter {

    private val scope by lazy { CoroutineScope(Dispatchers.Main) }

    override fun onCreate() {
        scope.launch() {
            try {
                mainView.showDeterminateBar(true)
                var savedListOfPosts = async(Dispatchers.IO) {
                   mainDataRepository.getPosts() ?: mutableListOf()
                }
                if (savedListOfPosts.await().isNullOrEmpty()) {
                    savedListOfPosts = async(Dispatchers.IO) {
                          mainRepository.getPosts()
                    }
                    val date = getDate()
                    mainView.showListFirstTime(savedListOfPosts.await(), date)
                    launch(Dispatchers.IO) {
                        mainDataRepository.savePosts(savedListOfPosts.await())
                    }
                } else {
                    mainView.showListFirstTime(savedListOfPosts.await(), "")
                }
            } catch (e: IOException) {
                mainView.showText(R.string.fail_io)
            } catch (e: GetPostsException) {
                mainView.showText(R.string.fail_loading_posts)
            } finally {
                mainView.showDeterminateBar(false)
            }
        }

    }

    override fun onElectedPostsButtonClicked() {
        scope.launch {
            try {
                val electedPosts = async(Dispatchers.IO) {
                    mainDataRepository.getElectedPosts()
                }
                mainView.showUpdateList(electedPosts.await() ?: mutableListOf(), "")
            } catch (e: IOException) {
                mainView.showText(R.string.fail_io)
            } finally {
                mainView.showDeterminateBar(false)
            }
        }
    }

    override fun onAllPostsButtonClicked() {
        scope.launch {
            try {
                val electedPosts = async(Dispatchers.IO) {
                    mainDataRepository.getPosts()
                }
                mainView.showUpdateList(electedPosts.await() ?: mutableListOf(), "")
            } catch (e: IOException) {
                mainView.showText(R.string.fail_io)
            } finally {
                mainView.showDeterminateBar(false)
            }
        }
    }

    override fun onStarButtonClicked(post: Post, position: Int) {
        scope.launch {
            try {
                mainView.showDeterminateBar(true)
                val electedPost = if (post.isElected == 0) {
                    post.copy(isElected = 1)
                } else {
                    post.copy(isElected = 0)
                }
                launch(Dispatchers.IO) {
                    mainDataRepository.addElectedPost(electedPost)
                }
                mainView.showStarClicked(electedPost, position)
            } catch (e: IOException) {
                mainView.showText(R.string.fail_io)
            } finally {
                mainView.showDeterminateBar(false)
            }
        }
    }

    override fun onRefreshClicked() {
        scope.launch {
            try {
                val savedListOfPosts = async(Dispatchers.IO) {
                    mainRepository.getPosts()
                }
                if (savedListOfPosts.await().isNotEmpty()) {
                    val electedPosts = async(Dispatchers.IO) {
                        mainDataRepository.getElectedPosts() ?: mutableListOf()
                    }
                    electedPosts.await().forEach { it1 ->
                        val index = savedListOfPosts.await().indexOfFirst { it.textId ==  it1.textId}
                        if (index != -1) {
                            val postCopy = savedListOfPosts.await()[index].copy(isElected = 1)
                            savedListOfPosts.await()[index] = postCopy
                        }
                    }
                    val date = getDate()
                    mainView.showUpdateListAfterRefresh(savedListOfPosts.await(), date)
                }
            } catch (e: IOException) {
                mainView.showText(R.string.fail_io)
            } catch (e: GetPostsException) {
                mainView.showText(R.string.fail_loading_posts)
            } finally {
                mainView.showStopRefreshing()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(Date())
    }
}