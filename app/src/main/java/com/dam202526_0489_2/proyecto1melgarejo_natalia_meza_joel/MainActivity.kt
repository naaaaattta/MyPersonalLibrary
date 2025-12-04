package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.adapters.BookAdapter
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model.Book
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.singleton.AppSingleton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Variables de la UI (Vista)
    private lateinit var rvBooks: RecyclerView
    private lateinit var fabAddBook: FloatingActionButton

    // Adaptador (El puente entre datos y vista)
    private lateinit var adapter: BookAdapter

    // Lista de libros en memoria
    private var bookList: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Aseguraos de que este XML existe y es el que os pasé antes

        // 1. Inicializamos las Vistas (Vincular con el XML)
        initViews()

        // 2. Configuramos el RecyclerView
        setupRecyclerView()

        // 3. Configuramos el botón flotante (+)
        setupFab()

        // 4. Cargamos los datos de la base de datos
        loadBooks()
    }

    // Es importante recargar la lista al volver (por si habéis añadido un libro nuevo y volvéis atrás)
    override fun onResume() {
        super.onResume()
        loadBooks()
    }

    private fun initViews() {
        rvBooks = findViewById(R.id.rvBooks)
        fabAddBook = findViewById(R.id.fabAddBook)
    }

    private fun setupRecyclerView() {
        // LayoutManager: Dice cómo se colocan los elementos (en lista vertical)
        rvBooks.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adaptador con una lista vacía al principio
        // y definimos qué pasa al hacer CLIC en un libro (lambda)
        adapter = BookAdapter(bookList) { bookSeleccionado ->
            // Aquí irá la navegación al DETALLE (lo haremos luego)
            Toast.makeText(this, "Clic en: ${bookSeleccionado.title}", Toast.LENGTH_SHORT).show()
        }

        rvBooks.adapter = adapter
    }

    private fun setupFab() {
        fabAddBook.setOnClickListener {
            // Aquí irá la navegación al FORMULARIO DE AÑADIR (lo haremos luego)
            // Por ahora mostramos un mensaje para probar que funciona
            Toast.makeText(this, "Ir a Añadir Libro", Toast.LENGTH_SHORT).show()

            // TRUCO PARA PROBAR: Descomenta estas líneas para probar si guarda en la base de datos
            val intent = Intent(this, com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.controllers.NewBookActivity::class.java)
            startActivity(intent)
        }
    }

    // --- LÓGICA DE BASE DE DATOS (MODELO) ---

    private fun loadBooks() {
        // Usamos lifecycleScope.launch para trabajar en segundo plano (Corrutinas)
        // Room NO permite hacer consultas en el hilo principal (bloquearía la app)
        lifecycleScope.launch {
            // 1. Pedimos la instancia de la BD al Singleton
            val database = AppSingleton.getInstance(this@MainActivity).database

            // 2. Pedimos la lista de libros al DAO
            val booksFromDb = database.bookDao().getAllBooks()

            // 3. Actualizamos la lista local y el adaptador
            bookList.clear()
            bookList.addAll(booksFromDb)
            adapter.updateData(bookList)

            // Si la lista está vacía, mostramos un aviso discreto (opcional)
            if (bookList.isEmpty()) {
                Toast.makeText(this@MainActivity, "No hay libros aún", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función temporal para probar que Room funciona (puedes borrarla luego)
    private fun saveBookTest(book: Book) {
        lifecycleScope.launch {
            val database = AppSingleton.getInstance(this@MainActivity).database
            database.bookDao().insertBook(book)
            loadBooks() // Recargamos la lista para ver el nuevo libro
            Toast.makeText(this@MainActivity, "Libro de prueba guardado", Toast.LENGTH_SHORT).show()
        }
    }
}