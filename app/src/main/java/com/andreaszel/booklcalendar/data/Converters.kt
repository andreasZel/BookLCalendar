package com.andreaszel.booklcalendar.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    @TypeConverter
    fun fromSource(source: CoverSource?): String? = source?.name

    @TypeConverter
    fun toSource(name: String?): CoverSource? = name?.let { CoverSource.valueOf(it) }
}
