package com.example.mycatcollections.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CatResponse(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("breeds")
	val breeds: List<BreedsItem?>? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
