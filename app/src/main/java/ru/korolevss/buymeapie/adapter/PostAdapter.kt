package ru.korolevss.buymeapie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korolevss.buymeapie.R
import ru.korolevss.buymeapie.model.Post

class PostAdapter(var list: MutableList<Post>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var starBtnClickListener: OnStarBtnClickListener? = null
    var showFooter: ShowFooter? = null

    interface ShowFooter {
        fun showFooter(view: View, amountAdapterElements: Int)
    }

    interface OnStarBtnClickListener {
        fun onStarBtnClicked(position: Int)
    }


    companion object {
        private const val TYPE_ITEM_POST = 0
        private const val ITEM_FOOTER = 1
    }

    fun updatePosts(newData: MutableList<Post>) {
        this.list.clear()
        this.list.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.post_card, parent, false)
        return when (viewType) {
            ITEM_FOOTER -> FooterViewHolder(
                this,
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.footer_item, parent, false)
            )
            else -> PostViewHolder(this, view)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            list.size -> ITEM_FOOTER
            else -> TYPE_ITEM_POST
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != list.size) {
            val post = list[position]
            with(holder as PostViewHolder) {
                bind(post)
            }
        }
    }

}