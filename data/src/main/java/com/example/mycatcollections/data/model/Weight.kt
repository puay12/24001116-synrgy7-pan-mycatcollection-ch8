package com.example.mycatcollections.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Weight(

    @field:SerializedName("metric")
    val metric: String? = null,

    @field:SerializedName("imperial")
    val imperial: String? = null
)
