package anton.miranouski.cats.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import anton.miranouski.cats.network.CatListPagingSource
import anton.miranouski.cats.network.CatService
import anton.miranouski.cats.network.RetrofitInstance

class CatListViewModel : ViewModel() {

    private val service: CatService = RetrofitInstance.getInstance().create(CatService::class.java)

    fun getCats() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CatListPagingSource(service) }
    ).flow.cachedIn(viewModelScope)
}