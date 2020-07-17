package ru.korolevss.buymeapie.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.post_card.view.*
import ru.korolevss.buymeapie.PostActivity
import ru.korolevss.buymeapie.R
import ru.korolevss.buymeapie.model.Post

class PostViewHolder(
    private val adapter: PostAdapter, private val view: View
) : RecyclerView.ViewHolder(view) {

    init {
        this.clickStarBtnListener()
        this.clickViewListener()
    }

    private companion object {
        const val IMAGE ="IMAGE"
        const val TITLE ="TITLE"
        const val DESCRIPTION ="DESCRIPTION"
        const val AUTHOR = "AUTHOR"
    }

    fun bind(post: Post) {
        with (view) {
            textViewTitlePostCard.text = post.title
            if (post.isElected == 1) {
                imageViewElectedPostCard.setImageResource(R.drawable.ic_star_24px)
            } else {
                imageViewElectedPostCard.setImageResource(R.drawable.ic_star_border_24px)
            }
            Glide.with(this)
                .load(post.image)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_error)
                .override(192, 192)
                .centerCrop()
                .into(imageViewPostCard)
        }
    }

    private fun clickStarBtnListener() {
        with (view) {
            imageViewElectedPostCard.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    adapter.starBtnClickListener?.onStarBtnClicked(adapterPosition)
                }
            }
        }
    }

    private fun clickViewListener() {
        view.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val post = adapter.list[adapterPosition]
                val intent = Intent(view.context, PostActivity::class.java)
                intent.putExtra(IMAGE, post.image)
                intent.putExtra(TITLE, post.title)
                intent.putExtra(DESCRIPTION, post.description)
                intent.putExtra(AUTHOR, post.author)
                view.context.startActivity(intent)
            }
        }
    }

}
