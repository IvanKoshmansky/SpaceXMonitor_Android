package com.example.android.spacexmonitor.webservice

import com.squareup.moshi.Json

//
// класс для разбора одного запуска в простом формате
//

data class ApiOneLaunchResponse (
    @Json(name = "links")
    val oneLaunchLinks: ApiOneLaunchLinks,
    val success: Boolean?,  // может быть null
    val name: String,       // название миссии
    @Json(name = "date_utc")
    val dateUtc: String,    // дата запуска в Utc
    val cores: List<ApiOneLaunchCore>,  // в первом элементе количество повторных использований первой ступени
    val id: String          // id запуска
)

data class ApiOneLaunchLinks (
    val patch: ApiOneLaunchPatch
)

data class ApiOneLaunchPatch (
    val small: String?,  // иконка миссии (url или null)
    val large: String?   // логотип миссии
)

data class ApiOneLaunchCore (
    val flight: Int?
)

// класс для разбора всего ответа с учетом пагинации
data class ApiLaunchesResponse (
    val docs: List<ApiOneLaunchResponse>,  // список запусков
    val totalDocs: Int,                 // общее количество запусков
    val hasPrevPage: Boolean,           // есть предыдущая страница
    val hasNextPage: Boolean,           // есть следующая страница
) {
    companion object {
        fun fillEmpty() = ApiLaunchesResponse(listOf(), 0, false, false)
    }
}

//
// класс для разбора данных о команде
//

data class ApiCrewMembersResponse (
    val docs: List<ApiCrewMember>  // если приходит пустой список, то это значит в запуске был беспилотный
)

data class ApiCrewMember (
    val name: String,
    val agency: String,
    val status: String
)

//
// класс для разбора деталей миссии в расширенном формате без пагинации
//

class ApiOneLaunchDetailResponse (
    @Json(name = "links")
    val oneLaunchLinks: ApiOneLaunchLinks,
    val success: Boolean?,  // может быть null
    val details: String?,   // может быть null
    val name: String,       // название миссии
    @Json(name = "date_utc")
    val dateUtc: String,    // дата запуска в Utc
    val cores: List<ApiOneLaunchCore>,  // в первом элементе количество повторных использований первой ступени
    val id: String          // id запуска
) {
    companion object {
        fun fillEmpty () = ApiOneLaunchDetailResponse(ApiOneLaunchLinks(ApiOneLaunchPatch("", "")),
            false, "", "", "", listOf(), "")
    }
}
