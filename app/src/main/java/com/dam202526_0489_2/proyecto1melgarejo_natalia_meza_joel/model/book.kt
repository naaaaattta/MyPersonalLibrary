package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "books_table")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var author: String,
    var year: Int,
    var coverUri: String? = null // Para guardar la ruta de la foto más adelante
) : Serializable