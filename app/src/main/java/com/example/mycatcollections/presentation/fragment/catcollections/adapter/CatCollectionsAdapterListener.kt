package com.example.mycatcollections.presentation.fragment.catcollections.adapter

import com.example.mycatcollections.data.model.Cat

interface CatCollectionsAdapterListener {
    fun onClickCard(data: Cat)
}