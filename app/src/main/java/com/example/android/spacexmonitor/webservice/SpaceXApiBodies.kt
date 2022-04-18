package com.example.android.spacexmonitor.webservice

import com.squareup.moshi.Json

//
// тело запроса для получения всех запусков начиная с даты date_utc с поддержкой пагинации
//

data class GetLaunchesBody (
    val query: GetLaunchesQuery,
    val options: GetLaunchesOptions,
)

data class GetLaunchesQuery (
    @Json(name = "date_utc")
    val dateUtc: GetLaunchesDateUtc
)

// Dates need to be ISO 8601 friendly for these operators to work properly
// "2017-06-22T00:00:00.000Z"
data class GetLaunchesDateUtc (
    @Json(name = "\$gte") // '\' - экранирование служебного символа
    val gte: String
)

// страницы нумеруются с единицы
data class GetLaunchesOptions (
    val page: Int,
    val sort: GetLaunchesSort
)

// сортировка по убыванию
data class GetLaunchesSort (
    @Json(name = "date_utc")
    val dateUtc: String = "desc"
)

//
// тело запроса для получения информации о команде
//

data class GetCrewMembersBody (
    val query: GetCrewFromLaunch
)

data class GetCrewFromLaunch (
    val launches: String
)
