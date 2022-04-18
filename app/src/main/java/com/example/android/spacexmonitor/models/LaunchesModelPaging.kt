package com.example.android.spacexmonitor.models

// представление данных по запускам для вью пейджинга
data class LaunchesModelPaging (
    val launches: List<OneLaunchModel>,
    val hasNext: Boolean,
    val hasPrev: Boolean
)
