package com.example.mycatcollections.presentation.fragment.catcollections.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mycatcollections.data.model.Cat
import com.example.mycatcollections.databinding.ItemCatBinding
import com.example.mycatcollections.presentation.fragment.catcollections.adapter.CatCollectionsAdapterListener

class CatCollectionsViewHolder(
    private val catCollectionsAdapterListener: CatCollectionsAdapterListener,
    private val binding: ItemCatBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: Cat) {
        binding.catImage.load(data.imgUrl)
        binding.catText.text = data.name
        binding.root.setOnClickListener{ catCollectionsAdapterListener.onClickCard(data.id) }
    }
}