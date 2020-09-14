package com.example.baseandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseandroid.databinding.ItemSearchBinding
import com.example.baseandroid.domain.SearchObject

class SearchAdapter: ListAdapter<SearchObject, SearchAdapter.ViewHolder>(SearchCallBack()) {

    var numPage = 1
    var wordSearched = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val itemBinding: ItemSearchBinding): RecyclerView.ViewHolder(itemBinding.root)  {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: SearchObject) {
            itemBinding.searchObject = item
            itemBinding.executePendingBindings()
        }
    }

}

class SearchCallBack: DiffUtil.ItemCallback<SearchObject>() {
    override fun areItemsTheSame(oldItem: SearchObject, newItem: SearchObject): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: SearchObject, newItem: SearchObject): Boolean {
        return oldItem == newItem
    }

}


