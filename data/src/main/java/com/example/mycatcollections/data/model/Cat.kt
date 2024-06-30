package com.example.mycatcollections.data.model

import androidx.annotation.Keep

@Keep
data class Cat(
    val id: String,
    val imgUrl: String,
    val name: String,
    val weight: String,
    val lifeSpan: String,
    val temperament: String,
    val origin: String,
    val description: String
)
