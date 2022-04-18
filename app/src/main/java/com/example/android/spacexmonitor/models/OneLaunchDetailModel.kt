package com.example.android.spacexmonitor.models

// модель для подробного описания конкретной миссии на втором экране
data class OneLaunchDetailModel (
    // простая модель
    val simple: OneLaunchModel,
    // расширения
    val imageUrl: String?,
    val details: String?,
    val crewMembers: List<CrewMember>
) {
    companion object {
        fun fillEmpty() = OneLaunchDetailModel(
            simple = OneLaunchModel(
                iconUrl = null,
                name = "",
                coresFlight = null,
                success = null,
                dateUtc = "",
                id = "",
            ),
            imageUrl = null,
            details = null,
            crewMembers = listOf()
        )
    }
}

data class CrewMember (
    val name: String,
    val agency: String,
    val status: String
)
