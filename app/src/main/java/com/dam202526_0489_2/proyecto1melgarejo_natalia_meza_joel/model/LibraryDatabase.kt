package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 1)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}