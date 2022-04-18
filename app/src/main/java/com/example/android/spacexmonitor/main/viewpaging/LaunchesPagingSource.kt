package com.example.android.spacexmonitor.main.viewpaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.spacexmonitor.LocalRepository
import com.example.android.spacexmonitor.PAGE_SIZE
import com.example.android.spacexmonitor.models.OneLaunchModel

// пейджинг сорс для вью педжинга на основном экране
class LaunchesPagingSource (private val localRepository: LocalRepository) : PagingSource<Int, OneLaunchModel>() {

    // основная функция подгрузки из репозитория
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OneLaunchModel> {

        val pageIndex = params.key ?: 0

        // номер страницы для загрузки (начиная с единицы) и количество строк (не страниц) передаем в репозиторий
        val cachedLaunches = localRepository.getCachedLaunches(pageIndex + 1, params.loadSize)

        var nextKey: Int? = null
        if (cachedLaunches.hasNext && (cachedLaunches.launches.size >= PAGE_SIZE)) {
            // следующая страница существует (в кэше или на сервере)
            nextKey = pageIndex + (cachedLaunches.launches.size / PAGE_SIZE)
        }

//        Note: It is valid for PagingSource.load to return a LoadResult that has a different
//        number of items than the requested load size.
        var prevKey: Int? = null
        if (pageIndex > 0) {
            prevKey = pageIndex - 1
        }

        return LoadResult.Page(
            // данные
            data = cachedLaunches.launches,
            // предыдущий номер страницы или null если не существует
            prevKey = prevKey,
            // номер следующей страницы или null если не существует
            nextKey = nextKey
        )
    }

//    Provide a Key used for the initial load for the next PagingSource due to invalidation
//    of this PagingSource. The Key is provided to load via LoadParams.key.
    override fun getRefreshKey(state: PagingState<Int, OneLaunchModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
