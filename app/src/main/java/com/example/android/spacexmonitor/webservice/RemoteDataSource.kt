package com.example.android.spacexmonitor.webservice

import com.example.android.spacexmonitor.models.LaunchesModelPaging
import com.example.android.spacexmonitor.models.OneLaunchModel
import java.lang.Exception
import javax.inject.Inject

// конкретная реализация удаленного датасорса

class RemoteDataSource @Inject constructor (val apiService: SpaceXApiService) : IRemoteDataSource {

    override suspend fun getLaunches(page: Int): LaunchesResponseWithResult {
        var apiResponse: ApiLaunchesResponse
        var remoteDataSourceResult: RemoteDataSourceResult
        val getLaunchesBody = GetLaunchesBody(GetLaunchesQuery(GetLaunchesDateUtc("2021-12-22T00:00:00.000Z")), GetLaunchesOptions(page, GetLaunchesSort()))
        try {
            apiResponse = apiService.getLaunches(getLaunchesBody).await()
            remoteDataSourceResult = RemoteDataSourceResult.DONE
        } catch (e: Exception) {
            e.printStackTrace()
            apiResponse = ApiLaunchesResponse.fillEmpty()
            remoteDataSourceResult = RemoteDataSourceResult.ERROR
        }
        return LaunchesResponseWithResult(
            launchesModelPaging = LaunchesModelPaging(
                launches = apiResponse.docs.map {
                    OneLaunchModel(
                        iconUrl = it.oneLaunchLinks.patch.small,
                        name = it.name,
                        coresFlight = it.cores.first().flight,
                        success = it.success,
                        dateUtc = it.dateUtc,
                        id = it.id
                    )
                },
                hasNext = apiResponse.hasNextPage,
                hasPrev = apiResponse.hasPrevPage
            ),
            result = remoteDataSourceResult
        )
    }

    override suspend fun getOneLaunchDetail(launchId: String): OneLaunchDetailResponseWithResult {
        var apiResponse: ApiOneLaunchDetailResponse
        var remoteDataSourceResult: RemoteDataSourceResult
        try {
            apiResponse = apiService.getOneLaunchDetail(launchId).await()
            remoteDataSourceResult = RemoteDataSourceResult.DONE
        } catch (e: Exception) {
            e.printStackTrace()
            apiResponse = ApiOneLaunchDetailResponse.fillEmpty()
            remoteDataSourceResult = RemoteDataSourceResult.ERROR
        }
        return OneLaunchDetailResponseWithResult(
            imageUrl = apiResponse.oneLaunchLinks.patch.large,
            name = apiResponse.name,
            coresFlight = apiResponse.cores.first().flight,
            details = apiResponse.details,
            success = apiResponse.success,
            dateUtc = apiResponse.dateUtc,
            id = apiResponse.id,
            result = remoteDataSourceResult
        )
    }

    override suspend fun getCrewMembers(launchId: String): CrewMembersResponseWithResult {
        var apiResponse: ApiCrewMembersResponse
        var remoteDataSourceResult: RemoteDataSourceResult
        val getCrewMembersBody = GetCrewMembersBody(GetCrewFromLaunch(launchId))
        try {
            apiResponse = apiService.getCrewMembers(getCrewMembersBody).await()
            remoteDataSourceResult = RemoteDataSourceResult.DONE
        } catch (e: Exception) {
            e.printStackTrace()
            apiResponse = ApiCrewMembersResponse(listOf()) // пустой список
            remoteDataSourceResult = RemoteDataSourceResult.ERROR
        }
        return CrewMembersResponseWithResult(
            crewMembers = apiResponse.docs.map {
                RemoteCrewMember(
                    name = it.name,
                    agency = it.agency,
                    status = it.status
                )
            },
            result = remoteDataSourceResult
        )
    }
}
