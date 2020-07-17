package ru.korolevss.buymeapie.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.korolevss.buymeapie.model.Post

class PostDiffUtilCallback(
    private val oldList: MutableList<Post>,
    private val newList: MutableList<Post>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.textId == newModel.textId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.image == newModel.image
                && oldModel.title == newModel.title
                && oldModel.isElected == newModel.isElected
    }

}