package ru.korolevss.buymeapie.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    val author: String,
    val description: String,
    val image: String,
    val rating: Float,
    @PrimaryKey
    val textId: String,
    val title: String,
    val isElected: Int = 0
)