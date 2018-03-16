package com.mingle.minglecoins.models.repository

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

class PricesListResponseDeserializer : JsonDeserializer<PricesListResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PricesListResponse? {
        val gson = Gson()
        json?.let {
            val jsonObject = json.asJsonObject
            val pricesJsonArrayObject = JsonArray()
            val symbolsJsonArrayObject = JsonArray()
            jsonObject.keySet().forEach {
                symbolsJsonArrayObject.add(it)
                if (jsonObject.has(it) && jsonObject.get(it).isJsonObject) {
                    val priceJsonObject = jsonObject.getAsJsonObject(it)
                    pricesJsonArrayObject.add(priceJsonObject)
                }
            }

            val newJsonObject = JsonObject()
            if (pricesJsonArrayObject.size() > 0) {
                newJsonObject.add("Prices", pricesJsonArrayObject)
            }
            if (symbolsJsonArrayObject.size() > 0) {
                newJsonObject.add("Symbols", symbolsJsonArrayObject)
            }
            return gson.fromJson(newJsonObject, PricesListResponse::class.java)
        }
        return null
    }
}

data class Price(
    @field:SerializedName("USD")
    val usd : String? = null
)

data class PricesListResponse(
        @field:SerializedName("Symbols")
        val symbols: ArrayList<String>? = null,

        @field:SerializedName("Prices")
        val prices: ArrayList<Price>? = null
)