package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.R
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model.Book

// Recibe la lista de libros y una función lambda para saber cuándo hacen clic en un libro
class BookAdapter(
    private var books: List<Book>,
    private val onBookClicked: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewHolder: Mantiene las referencias a los TextViews
    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
        val tvYear: TextView = view.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        // "Inflamos" el diseño del item_book.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author
        holder.tvYear.text = book.year.toString()

        // Configuramos el clic
        holder.itemView.setOnClickListener { onBookClicked(book) }
    }

    override fun getItemCount() = books.size

    // Método para actualizar la lista cuando cambien los datos
    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}