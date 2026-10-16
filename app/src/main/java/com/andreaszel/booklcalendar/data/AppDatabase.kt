package com.andreaszel.booklcalendar.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Book::class], version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        fun create(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "books.db").build()
    }
}
