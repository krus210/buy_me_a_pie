package ru.korolevss.buymeapie

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.footer_item.view.*
import ru.korolevss.buymeapie.adapter.PostAdapter
import ru.korolevss.buymeapie.adapter.PostDiffUtilCallback
import ru.korolevss.buymeapie.contract.MainContract
import ru.korolevss.buymeapie.model.Post
import ru.korolevss.buymeapie.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainContract.View, PostAdapter.OnStarBtnClickListener,
    PostAdapter.ShowFooter {

    private val mainPresenter by lazy {
        MainPresenter(
            this
        )
    }

    companion object {
        private const val SHARED_PREF_KEY = "SHARED_PREF"
        private const val DATE = "DATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenter.onCreate()

        buttonShowElectedPosts.setOnClickListener {
            buttonShowElectedPosts.setImageResource(R.drawable.ic_star_24px)
            buttonShowAllPosts.setImageResource(R.drawable.ic_article_black_24dp)
            swipeContainer.isEnabled = false
            mainPresenter.onElectedPostsButtonClicked()
        }

        buttonShowAllPosts.setOnClickListener {
            buttonShowElectedPosts.setImageResource(R.drawable.ic_star_border_24px)
            buttonShowAllPosts.setImageResource(R.drawable.ic_article_24px)
            swipeContainer.isEnabled = true
            mainPresenter.onAllPostsButtonClicked()
        }
        swipeContainer.setOnRefreshListener {
            mainPresenter.onRefreshClicked()
        }
    }

    override suspend fun showUpdateListAfterRefresh(list: MutableList<Post>, date: String) {
        if (date.isNotEmpty()) {
            saveDate(date)
        }
        updateRecyclerView(list)
    }

    override suspend fun showStopRefreshing() {
        swipeContainer.isRefreshing = false
    }

    override suspend fun showText(message: Int) {
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    override suspend fun showListFirstTime(list: MutableList<Post>, date: String) {
        if (date.isNotEmpty()) {
            saveDate(date)
        }
        with(container) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostAdapter(list).apply {
                starBtnClickListener = this@MainActivity
                showFooter = this@MainActivity
            }
        }
    }

    override suspend fun showUpdateList(list: MutableList<Post>, date: String) {
        if (date.isNotEmpty()) {
            saveDate(date)
        }
        updateRecyclerView(list)
    }

    private fun updateRecyclerView(newList: MutableList<Post>) {
        with(container.adapter as PostAdapter) {
            val postDiffUtilCallback = PostDiffUtilCallback(list, newList)
            val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
            updatePosts(newList)
            postDiffResult.dispatchUpdatesTo(this)
        }
    }

    override suspend fun showDeterminateBar(isShowed: Boolean) {
        if (isShowed) {
            determinateBar.isVisible = true
            buttonShowAllPosts.isEnabled = false
            buttonShowElectedPosts.isEnabled = false
        } else {
            determinateBar.isVisible = false
            buttonShowAllPosts.isEnabled = true
            buttonShowElectedPosts.isEnabled = true
        }
    }

    private fun saveDate(date: String) {
        val sharedPref = getSharedPreferences(
            SHARED_PREF_KEY,
            Context.MODE_PRIVATE
        )
        sharedPref.edit {
            putString(DATE, date)
        }
    }

    private fun getDate() = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        .getString(DATE, "") ?: ""

    @SuppressLint("SetTextI18n")
    override fun showFooter(view: View, amountAdapterElements: Int) {
        view.textViewFooter.text =
            "${getString(R.string.at_all)} $amountAdapterElements${getString(R.string.last_update)} ${getDate()} "
    }

    override fun onStarBtnClicked(position: Int) {
        val post = (container.adapter as PostAdapter).list[position]
        mainPresenter.onStarButtonClicked(post, position)
    }

    override suspend fun showStarClicked(post: Post, position: Int) {
        with(container.adapter as PostAdapter) {
            list[position] = post
            if (buttonShowElectedPosts.drawable.constantState ==
                resources.getDrawable(R.drawable.ic_star_24px).constantState
            ) {
                notifyItemRemoved(position)
                list.remove(post)
            } else {
                notifyItemChanged(position)
            }

        }
    }
}