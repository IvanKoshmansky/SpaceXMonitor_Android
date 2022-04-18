package com.example.android.spacexmonitor.webservice

import com.example.android.spacexmonitor.models.LaunchesModelPaging

// результаты запросов в преобразованном формате (без лишней вложенности как в JSON) + результат

data class LaunchesResponseWithResult (
    val launchesModelPaging: LaunchesModelPaging,
    val result: RemoteDataSourceResult
)

data class OneLaunchDetailResponseWithResult (
    val imageUrl: String?,
    val name: String,
    val coresFlight: Int?,
    val details: String?,
    val success: Boolean?,
    val dateUtc: String,
    val id: String,
    val result: RemoteDataSourceResult
)

data class CrewMembersResponseWithResult (
    val crewMembers: List<RemoteCrewMember>,
    val result: RemoteDataSourceResult
)

data class RemoteCrewMember (
    val name: String,
    val agency: String,
    val status: String
)
