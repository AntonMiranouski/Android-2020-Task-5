package anton.miranouski.cats.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatDto(
    @Json(name = "id") val id: String?,
    @Json(name = "url") val imageUrl: String?
)
