package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.singleton

import android.content.Context
import androidx.room.Room
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model.LibraryDatabase

class AppSingleton private constructor(context: Context) {

    // Instancia de la base de datos dentro del Singleton
    val database: LibraryDatabase = Room.databaseBuilder(
        context.applicationContext,
        LibraryDatabase::class.java,
        "my_library_db"
    ).build()

    companion object {
        @Volatile
        private var INSTANCE: AppSingleton? = null

        // Método para obtener la instancia única del Singleton
        fun getInstance(context: Context): AppSingleton {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppSingleton(context).also { INSTANCE = it }
            }
        }
    }
}