package com.example.android.spacexmonitor

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("nullableIntToText")
fun TextView.nullableIntToText(value: Int?) {
    text = if (value != null) value.toString() else "Нет данных"
}

@BindingAdapter("nullableStatusToText")
fun TextView.nullableStatusToText(success: Boolean?) {
    text = if (success != null) {
        if (success) "Успешно" else "Не успешно"
    } else "Нет данных"
}

// образец: 2022-07-01T00:00:00
private val utcRegex = Regex("""(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)""")

// формат для Overview
@BindingAdapter("overviewDateFormattedFromUtc")
fun TextView.dateFromUtc_DD_MM_YYYY(dateUtc: String) {
    var result = ""
    val matchResult = utcRegex.find(dateUtc)
    if (matchResult != null) {
        val values = matchResult.groupValues.drop(1) // первый элемент в результате не нужен
        // ДД-ММ-ГГГГ
        result = "${values[2]}-${values[1]}-${values[0]}"
    }
    text = result
}

// формат для Detail
@BindingAdapter("detailDateFormattedFromUtc")
fun TextView.dateFromUtc_HH_MM_DD_MM_YYYY(dateUtc: String) {
    var result = ""
    val matchResult = utcRegex.find(dateUtc)
    if (matchResult != null) {
        val values = matchResult.groupValues.drop(1) // первый элемент в результате не нужен
        // ЧЧ:ММ ДД-ММ-ГГГГ
        result = "${values[3]}:${values[4]} ${values[2]}-${values[1]}-${values[0]}"
    }
    text = result
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
