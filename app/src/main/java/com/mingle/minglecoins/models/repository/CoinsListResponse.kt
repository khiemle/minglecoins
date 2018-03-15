package com.mingle.minglecoins.models.repository

import android.util.Log
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.mingle.minglecoins.models.Coin
import java.lang.reflect.Type


class CoinsListResponseDeserializer : JsonDeserializer<CoinsListResponse> {
	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CoinsListResponse? {
		val gson = Gson()
		json?.let {
			val jsonObject = json.getAsJsonObject()
			if (jsonObject.has("Data") && jsonObject.get("Data").isJsonObject) {
				val dataJsonObject = jsonObject.getAsJsonObject("Data")
				jsonObject.remove("Data")

				val dataJsonArrayObject = JsonArray()
				dataJsonObject.keySet().forEach {
					if (dataJsonObject.has(it) && dataJsonObject.get(it).isJsonObject) {
						val coinJsonObject = dataJsonObject.getAsJsonObject(it)
						dataJsonArrayObject.add(coinJsonObject)
					}
				}
				if (dataJsonArrayObject.size() > 0) {
					jsonObject.add("Data", dataJsonArrayObject)
				}

			}
			return gson.fromJson(jsonObject, CoinsListResponse::class.java)
		}
		return null
	}
}

data class CoinsListResponse(

	@field:SerializedName("Response")
	val response: String? = null,

	@field:SerializedName("BaseLinkUrl")
	val baseLinkUrl: String? = null,

	@field:SerializedName("Type")
	val type: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("DefaultWatchlist")
	val defaultWatchlist: DefaultWatchlist? = null,

	@field:SerializedName("BaseImageUrl")
	val baseImageUrl: String? = null,

	@field:SerializedName("Data")
	val data: ArrayList<Coin>? = null
)