package com.example.android.spacexmonitor

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

// статус миссии с поддержкой null
// "Успешно/Не успешно/Нет данных о статусе"
@BindingAdapter("nullableStatusToText")
fun TextView.nullableStatusToText(success: Boolean?) {
    text = if (success != null) {
        if (success) "Успешно" else "Не успешно"
    } else "Нет данных о статусе"
}

// количество повторных использований первой ступени с поддержкой null
// "Количество повторных использований первой ступени: <value>/не данных"
@BindingAdapter("nullableCoresFlightToText")
fun TextView.nullableIntToText(value: Int?) {
    var str = context.getText(R.string.cores_flight_prefix).toString()
    str += " "
    str += value ?: "нет данных"
    text = str
}

// образец: 2022-07-01T00:00:00
private val utcRegex = Regex("""(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)""")

// преобразование даты запуска миссии для первого экрана
@BindingAdapter("overviewDateFormattedFromUtc")
fun TextView.dateFromUtc_DD_MM_YYYY(dateUtc: String) {
    var result = ""
    val matchResult = utcRegex.find(dateUtc)
    if (matchResult != null) {
        val values = matchResult.groupValues.drop(1) // первый элемент в результате не нужен
        // ДД-ММ-ГГГГ
        result = "${values[2]}-${values[1]}-${values[0]}"
    }
    var str = context.getText(R.string.mission_date_overview_prefix).toString()
    str += " $result"
    text = str
}

// формат для второго экрана
@BindingAdapter("detailDateFormattedFromUtc")
fun TextView.dateFromUtc_HH_MM_DD_MM_YYYY(dateUtc: String) {
    var result = ""
    val matchResult = utcRegex.find(dateUtc)
    if (matchResult != null) {
        val values = matchResult.groupValues.drop(1) // первый элемент в результате не нужен
        // ЧЧ:ММ ДД-ММ-ГГГГ
        result = "${values[3]}:${values[4]} ${values[2]}-${values[1]}-${values[0]}"
    }
    var str = context.getText(R.string.mission_date_overview_prefix).toString()
    str += " $result"
    text = str
}

// преобразование деталей миссии с возможностью значения null
@BindingAdapter("nullableDetailsToText")
fun TextView.nullableDetailsToText(details: String?) {
    text = details ?: "Нет данных"
}

// загрузка картинок через Coil (функция расширения ImageView)
@BindingAdapter("loadFromUrl")
fun ImageView.loadFromUrl(url: String?) {
    if (url != null) {
        // есть иконка
        load(url) {
            // плэйсхолдер показывается во время загрузки
            placeholder(R.drawable.ic_about)
        }
    } else {
        // иконки нет, оставляем плэйсхолдер
        // важный нюанс: если использовать Coil, то для программного задания картинки из ресурсов
        // надо тоже использовать load
        load(R.drawable.ic_about)
    }
}
