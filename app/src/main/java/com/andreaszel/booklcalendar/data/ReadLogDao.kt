package com.andreaszel.booklcalendar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ReadLogDao {
    @Query("SELECT * FROM read_logs WHERE date BETWEEN :from AND :to ORDER BY date, createdAt")
    fun observeRange(from: LocalDate, to: LocalDate): Flow<List<ReadLog>>

    @Query("SELECT * FROM read_logs WHERE date = :date ORDER BY createdAt")
    fun observeDay(date: LocalDate): Flow<List<ReadLog>>

    @Query("SELECT * FROM read_logs WHERE bookId = :bookId ORDER BY date DESC")
    fun observeForBook(bookId: Long): Flow<List<ReadLog>>

    @Query("SELECT * FROM read_logs ORDER BY date")
    fun observeAll(): Flow<List<ReadLog>>

    @Query("SELECT DISTINCT date FROM read_logs ORDER BY date")
    fun observeDates(): Flow<List<LocalDate>>

    @Query("SELECT * FROM read_logs WHERE bookId = :bookId AND date = :date")
    suspend fun get(bookId: Long, date: LocalDate): ReadLog?

    @Insert
    suspend fun insert(log: ReadLog): Long

    @Update
    suspend fun update(log: ReadLog)

    @Delete
    suspend fun delete(log: ReadLog)
}
