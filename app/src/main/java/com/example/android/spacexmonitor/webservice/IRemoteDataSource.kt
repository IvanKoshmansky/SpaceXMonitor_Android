package com.example.android.spacexmonitor.webservice

// абстрактный удаленный датасорс
// наследование через интерфейс позволяет соблюдать принцип OCP (SOLID)
// отдельные интерфейсы для локального (LocalDatabase) и удаленного датасорса позволяют соблюдать принцип ISP (SOLID)

// page: номер страницы (количество запусков на странице фиксировано и равно 10)
interface IRemoteDataSource {
    suspend fun getLaunches(page: Int): LaunchesResponseWithResult
    suspend fun getOneLaunchDetail(launchId: String): OneLaunchDetailResponseWithResult
    suspend fun getCrewMembers(launchId: String): CrewMembersResponseWithResult
}

enum class RemoteDataSourceResult { LOADING, ERROR, DONE }
