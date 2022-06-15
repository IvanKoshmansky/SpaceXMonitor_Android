package com.example.android.spacexmonitor

import androidx.lifecycle.MutableLiveData
import com.example.android.spacexmonitor.localdb.*
import com.example.android.spacexmonitor.webservice.*
import com.example.android.spacexmonitor.models.*
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

// в репозиторий проведены зависимости на два интерфейса: локальный источник данных и удаленный источник данных
class LocalRepository @Inject constructor (val localDataSource: LocalDatabase, val remoteDataSource: IRemoteDataSource) {

    // key - номер запрашиваемой страницы начиная с 1
    // loadSize - количество строк для запроса (не страниц а именно строк)
    suspend fun getCachedLaunches(key: Int, loadSize: Int): LaunchesModelPaging {
        return withContext(Dispatchers.IO) {
            // переключение диспетчера на IO в контексте + "прямой" возврат значения из корутины без применения Deferred
            val beginRow = (key - 1) * PAGE_SIZE
            val endRow = beginRow + loadSize - 1
            // считать из кэша
            var launchesFromCache = localDataSource.databaseDao.getLaunches(beginRow, endRow)
            // номер страницы для запроса на сервер, всегда запрашивается по одной странице (на странице по 10 записей)
            var remoteKey: Int? = null
            if (launchesFromCache.isEmpty()) {
                // кэш пуст - запросить на сервер с текущей страницы
                remoteKey = key
            } else {
                if ((launchesFromCache.size < loadSize) && (launchesFromCache.last().hasNext != 0)) {
                    // данных недостаточно, причем должны быть следующие данные - запросить их
                    remoteKey = if (launchesFromCache.size < PAGE_SIZE) key else key + 1
                }
            }
            remoteKey?.let { key ->
                // нужно подгрузить с сервера очередную страницу
                val remoteResponse = remoteDataSource.getLaunches(key)
                if (remoteResponse.result == RemoteDataSourceResult.DONE) {
                    // успешно подгрузилось - сохранить в БД
                    val launchesDB = mutableListOf<DBOneLaunch>()
                    var ii = 0
                    remoteResponse.launchesModelPaging.launches.forEach {
                        launchesDB.add(
                            DBOneLaunch(
                                // просчет номера строки для кэша начиная с 0
                                rowIndex = ((key - 1) * PAGE_SIZE + ii).toLong(),
                                hasNext = if (remoteResponse.launchesModelPaging.hasNext) 1 else 0,
                                iconUrl = it.iconUrl,
                                name = it.name,
                                coresFlight = it.coresFlight,
                                success = if (it.success != null) { if (it.success == true) 1 else 0 } else null,
                                dateUtc = it.dateUtc,
                                id = it.id
                            )
                        )
                        ii++
                    }
                    localDataSource.databaseDao.insertLaunches(*launchesDB.toTypedArray())
                    // повторно прочитать из кэша c учетом новой подгрузки
                    launchesFromCache = localDataSource.databaseDao.getLaunches(beginRow, endRow)
                }
            }

            // основная модель для UI:
            val launchesDomainModel = launchesFromCache.map {
                OneLaunchModel(
                    iconUrl = it.iconUrl,
                    name = it.name,
                    coresFlight = it.coresFlight,
                    success = if (it.success != null) { it.success != 0 } else null,
                    dateUtc = it.dateUtc,
                    id = it.id
                )
            }
            // + управляющие ограничения для пейджера (hasNext, hasPrev)
            val hasNext = launchesFromCache.last().hasNext != 0
            val hasPrev = key != 1
            // в итоге окончательный результат передаем из localDB, "single source of truth"
            val launchesModelPaging = LaunchesModelPaging(launchesDomainModel, hasNext, hasPrev)
            launchesModelPaging
        }
    }

    fun invalidateOneLaunchDetail() {
        // перед открытием второго экрана
        detailLiveData.value = OneLaunchDetailModel.fillEmpty()
    }
    val detailLiveData = MutableLiveData<OneLaunchDetailModel>().apply {
        // при первом создании
        value = OneLaunchDetailModel.fillEmpty()
    }
    // CrewMembers приходит от 08.04.2022
    suspend fun refreshOneLaunchDetail(launchId: String) {
        withContext(Dispatchers.IO) {
            val oneLaunchResponse = remoteDataSource.getOneLaunchDetail(launchId)
            if (oneLaunchResponse.result == RemoteDataSourceResult.DONE) {
                val crewMembersResponse = remoteDataSource.getCrewMembers(launchId)
                if (crewMembersResponse.result == RemoteDataSourceResult.DONE) {
                    // оба запроса успешны - собираем результат и грузим в LiveData
                    val oneLaunchDetail = OneLaunchDetailModel(
                        simple = OneLaunchModel(
                            iconUrl = "",
                            name = oneLaunchResponse.name,
                            coresFlight = oneLaunchResponse.coresFlight,
                            success = oneLaunchResponse.success,
                            dateUtc = oneLaunchResponse.dateUtc,
                            id = oneLaunchResponse.id
                        ),
                        imageUrl = oneLaunchResponse.imageUrl,
                        details = oneLaunchResponse.details,
                        crewMembers = crewMembersResponse.crewMembers.map {
                            CrewMember(it.name, it.agency, it.status)
                        }
                    )
                    // здесь postValue, потому что из IO треда в MAIN (в LiveData)
                    detailLiveData.postValue(oneLaunchDetail)
                }
            }
        }
    }

    suspend fun clearLocalCache() {
        // диспетчер будет переключается внутри рума, если в DAO объявлен suspend fun
        withContext(Dispatchers.IO) {
            localDataSource.databaseDao.clearTable()
        }
    }
}
