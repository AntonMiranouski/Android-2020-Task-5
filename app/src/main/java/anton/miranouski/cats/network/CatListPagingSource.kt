package anton.miranouski.cats.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import anton.miranouski.cats.model.Cat
import retrofit2.HttpException
import java.io.IOException

class CatListPagingSource(private val service: CatService) : PagingSource<Int, Cat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        val position = params.key ?: INITIAL_PAGE_NUMBER

        return try {
            val response = service.getListOfCats(page = position.toString())

            val cats = response.map { result ->
                Cat(
                    result.id,
                    result.imageUrl
                )
            }

            LoadResult.Page(
                data = cats,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (cats.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {

        const val INITIAL_PAGE_NUMBER = 1
    }
}