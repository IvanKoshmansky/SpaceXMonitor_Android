package com.example.android.spacexmonitor.main

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.android.spacexmonitor.LocalRepository
import com.example.android.spacexmonitor.PAGE_SIZE
import com.example.android.spacexmonitor.main.viewpaging.*
import com.example.android.spacexmonitor.models.OneLaunchDetailModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor (
    application: Application, val localRepository: LocalRepository
) : AndroidViewModel(application) {

    private var pager = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE
        ),
        pagingSourceFactory = { LaunchesPagingSource(localRepository) }
    )
    // каждый раз при рефреше адаптера фабрика должна создавать новый PagingSource
    // поэтому подставляем лямбда выражение, а не свойство

    // TODO: разобраться с этим преобразованием
    val overviewLiveData = pager.flow.cachedIn(viewModelScope).asLiveData()
    private val _needToRefreshAdapter = MutableLiveData<Boolean>()
    val needToRefreshAdapter
        get() = _needToRefreshAdapter
    fun needToRefreshAdapterReset() {
        _needToRefreshAdapter.value = false
    }

    // при смене конфигурации (повороте экрана) LiveData отрабатывает реактивно по всем цепочкам
    val detailLiveData = localRepository.detailLiveData

    fun setupOneLaunchDetails(launchId: String) {
        viewModelScope.launch {
            localRepository.refreshOneLaunchDetail(launchId)
        }
    }

    // навигация на второй фрагмент
    private val _navigateToLaunchDetail = MutableLiveData<String?>()
    val navigateToLaunchDetail
        get() = _navigateToLaunchDetail

    fun onItemClicked(id: String) {
        // Id миссии передается из callback адаптера, который вызывает адаптер
        _navigateToLaunchDetail.value = id
    }

    fun toLaunchDetailNavigated() {
        _navigateToLaunchDetail.value = null
    }

    fun clearOverviewCache() {
        viewModelScope.launch {
            localRepository.clearLocalCache()
            _needToRefreshAdapter.setValue(true) // Dispatcher.MAIN во ViewModelScope по умолчанию
        }
    }
}
