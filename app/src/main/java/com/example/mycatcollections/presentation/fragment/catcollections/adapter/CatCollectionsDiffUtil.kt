package com.example.mycatcollections.presentation.fragment.catcollections.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mycatcollections.data.model.Cat

class CatCollectionsDiffUtil : DiffUtil.ItemCallback<Cat>() {
    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}