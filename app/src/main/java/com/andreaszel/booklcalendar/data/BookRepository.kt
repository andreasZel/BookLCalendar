package com.andreaszel.booklcalendar.data

import kotlinx.coroutines.flow.Flow

class BookRepository(private val dao: BookDao, private val covers: CoverStore) {
    fun observeAll(): Flow<List<Book>> = dao.observeAll()

    fun observe(id: Long): Flow<Book?> = dao.observe(id)

    suspend fun get(id: Long): Book? = dao.get(id)

    suspend fun save(book: Book): Long {
        if (book.id == 0L) return dao.insert(book)
        val previous = dao.get(book.id)
        dao.update(book)
        if (previous?.coverPath != book.coverPath) covers.delete(previous?.coverPath)
        return book.id
    }

    suspend fun delete(book: Book) {
        dao.delete(book)
        covers.delete(book.coverPath)
    }
}
