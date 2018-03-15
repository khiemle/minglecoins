package com.mingle.minglecoins.models.repository

import com.google.gson.annotations.SerializedName

data class DefaultWatchlist(

	@field:SerializedName("CoinIs")
	val coinIs: String? = null,

	@field:SerializedName("Sponsored")
	val sponsored: String? = null
)