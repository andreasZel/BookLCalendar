package com.andreaszel.booklcalendar.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "read_logs", foreignKeys = [ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["bookId", "date"], unique = true), Index(value = ["date"])])
data class ReadLog(@PrimaryKey(autoGenerate = true) val id: Long = 0, val bookId: Long, val date: LocalDate, val pagesRead: Int, val note: String = "", val createdAt: Long = System.currentTimeMillis()) {
    companion object {
        const val NOTE_LIMIT = 280
    }
}
