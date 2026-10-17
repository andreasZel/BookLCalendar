package com.andreaszel.booklcalendar.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

class ReadLogRepository(private val dao: ReadLogDao) {
    fun observeMonth(month: YearMonth): Flow<List<ReadLog>> = dao.observeRange(month.atDay(1), month.atEndOfMonth())

    fun observeDay(date: LocalDate): Flow<List<ReadLog>> = dao.observeDay(date)

    fun observeForBook(bookId: Long): Flow<List<ReadLog>> = dao.observeForBook(bookId)

    fun observeAll(): Flow<List<ReadLog>> = dao.observeAll()

    suspend fun get(bookId: Long, date: LocalDate): ReadLog? = dao.get(bookId, date)

    // One log per book per day, an existing one is overwritten
    suspend fun save(log: ReadLog): Long {
        val clean = log.copy(pagesRead = log.pagesRead.coerceAtLeast(0), note = log.note.trim().take(ReadLog.NOTE_LIMIT))
        val existing = dao.get(clean.bookId, clean.date)
        if (existing == null) return dao.insert(clean.copy(id = 0))
        dao.update(clean.copy(id = existing.id, createdAt = existing.createdAt))
        return existing.id
    }

    suspend fun delete(log: ReadLog) = dao.delete(log)
}
