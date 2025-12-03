package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {
    // Obtener todos los libros para el RecyclerView
    @Query("SELECT * FROM books_table")
    suspend fun getAllBooks(): List<Book>

    // Obtener un libro por ID (útil para el detalle)
    @Query("SELECT * FROM books_table WHERE id = :id")
    suspend fun getBookById(id: Long): Book

    @Insert
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}