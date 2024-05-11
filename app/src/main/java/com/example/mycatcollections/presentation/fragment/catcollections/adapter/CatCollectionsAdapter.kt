package com.example.mycatcollections.presentation.fragment.catcollections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mycatcollections.data.model.Cat
import com.example.mycatcollections.databinding.ItemCatBinding
import com.example.mycatcollections.presentation.fragment.catcollections.viewholder.CatCollectionsViewHolder

class CatCollectionsAdapter(
    private val catCollectionsAdapterListener: CatCollectionsAdapterListener
) : ListAdapter<Cat, CatCollectionsViewHolder>(CatCollectionsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatCollectionsViewHolder {
        return CatCollectionsViewHolder(
            binding = ItemCatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            catCollectionsAdapterListener = catCollectionsAdapterListener
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(viewHolder: CatCollectionsViewHolder, position: Int) {
        viewHolder.render(getItem(position))
    }
}