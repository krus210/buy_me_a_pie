package ru.korolevss.buymeapie.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FooterViewHolder(adapter: PostAdapter, view: View) :
    RecyclerView.ViewHolder(view)  {

    init {
        adapter.showFooter?.showFooter(view, adapter.itemCount)
    }

}
