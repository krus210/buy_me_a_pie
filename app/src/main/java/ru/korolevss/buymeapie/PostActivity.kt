package ru.korolevss.buymeapie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    private companion object {
        const val IMAGE ="IMAGE"
        const val TITLE ="TITLE"
        const val DESCRIPTION ="DESCRIPTION"
        const val AUTHOR = "AUTHOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val image = intent.getStringExtra(IMAGE) ?: ""
        val title = intent.getStringExtra(TITLE) ?: ""
        val description = intent.getStringExtra(DESCRIPTION) ?: ""
        val author = intent.getStringExtra(AUTHOR) ?: ""

        if (image.isNotEmpty()) {
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(imageViewPostItem)
        }
        if (title.isNotEmpty()) {
            supportActionBar?.title = title
        }
        textViewAuthor.text = author
        textViewDescription.text = description
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}