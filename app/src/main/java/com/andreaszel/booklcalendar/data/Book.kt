package com.andreaszel.booklcalendar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "books")
data class Book(@PrimaryKey(autoGenerate = true) val id: Long = 0, val title: String, val description: String, val readDate: LocalDate, val coverPath: String?, val coverSource: CoverSource?, val createdAt: Long = System.currentTimeMillis())

enum class CoverSource { Camera, Gallery }
