package anton.miranouski.cats.network

import anton.miranouski.cats.model.CatDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatService {

    @GET("/v1/images/search")
    suspend fun getListOfCats(
        @Query("limit") limit: String = DEFAULT_LIMIT,
        @Query("page") page: String,
        @Query("order") order: String = DEFAULT_ORDER,
        @Query("mime_types") types: String = DEFAULT_TYPES
    ): List<CatDto>

    companion object {

        const val DEFAULT_LIMIT = "20"
        const val DEFAULT_ORDER = "Desc"
        const val DEFAULT_TYPES = "jpg,png"
    }
}