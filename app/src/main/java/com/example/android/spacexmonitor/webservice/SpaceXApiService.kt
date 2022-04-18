package com.example.android.spacexmonitor.webservice

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.spacexdata.com/"

private const val LAUNCHES_QUERY = "v4/launches/query"
private const val LAUNCHES = "v4/launches/"
private const val CREW_QUERY = "v4/crew/query"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface SpaceXApiService {
    // через тело запроса в формате JSON
    @POST(LAUNCHES_QUERY)
    fun getLaunches(@Body getLaunchesBody: GetLaunchesBody): Deferred<ApiLaunchesResponse>

    // id запуска является частью пути
    @GET(LAUNCHES + "{id}")
    fun getOneLaunchDetail(@Path("id") launchId: String): Deferred<ApiOneLaunchDetailResponse>

    @POST(CREW_QUERY)
    fun getCrewMembers(@Body getCrewMembersBody: GetCrewMembersBody): Deferred<ApiCrewMembersResponse>
}

object SpaceXApi {
    val spaceXApiService : SpaceXApiService by lazy { retrofit.create(SpaceXApiService::class.java) }
}

fun getSpaceXApiService(): SpaceXApiService = SpaceXApi.spaceXApiService
