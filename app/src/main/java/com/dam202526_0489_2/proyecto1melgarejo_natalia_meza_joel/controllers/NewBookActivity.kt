package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.controllers

import androidx.core.os.BundleCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.R
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.model.Book
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.singleton.AppSingleton
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class NewBookActivity : AppCompatActivity() {

    // --- 1. Variables de la Vista (UI) ---
    private lateinit var ivBookCover: ImageView
    private lateinit var btnTakePhoto: Button
    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var etYear: EditText
    private lateinit var btnSave: Button

    // Variable para guardar la ruta de la foto (String) y meterla en la BD
    private var currentPhotoPath: String? = null

    // --- 2. Launcher de la Cámara (Según temario AD11) ---
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Recuperamos el Bundle de extras (si existe)
            val extras = result.data?.extras

            // Usamos BundleCompat para obtener el Parcelable (Bitmap) de forma segura en cualquier versión
            val imageBitmap = extras?.let {
                BundleCompat.getParcelable(it, "data", Bitmap::class.java)
            }

            imageBitmap?.let {
                // A. La mostramos en pantalla
                ivBookCover.setImageBitmap(it)

                // B. La guardamos en almacenamiento interno
                currentPhotoPath = saveImageToInternalStorage(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)

        initViews()
        setupListeners()

        fillSampleData()
    }

    private fun initViews() {
        ivBookCover = findViewById(R.id.ivBookCover)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        etTitle = findViewById(R.id.etTitle)
        etAuthor = findViewById(R.id.etAuthor)
        etYear = findViewById(R.id.etYear)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupListeners() {
        // Botón CÁMARA
        btnTakePhoto.setOnClickListener {
            checkCameraPermission()
        }

        // Botón GUARDAR
        btnSave.setOnClickListener {
            saveBookToDatabase()
        }
    }

    // --- LÓGICA DE CÁMARA Y PERMISOS ---

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Pedimos permiso si no lo tenemos (AD11)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            // Ya tenemos permiso, abrir cámara
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Verificamos que haya una app de cámara antes de lanzarla
        if (intent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(this, "No se encontró app de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // Función auxiliar: Guarda el Bitmap en un fichero interno y devuelve la ruta (String)
    // Esto es necesario porque Room no puede guardar imágenes, solo texto (rutas)
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val filename = "book_cover_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, filename)
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    // Función para rellenar datos de prueba aleatorios
    private fun fillSampleData() {
        val samples = listOf(
            Triple("Cien años de soledad", "Gabriel García Márquez", "1967"),
            Triple("1984", "George Orwell", "1949"),
            Triple("El Principito", "Antoine de Saint-Exupéry", "1943"),
            Triple("Don Quijote de la Mancha", "Miguel de Cervantes", "1605"),
            Triple("Harry Potter y la piedra filosofal", "J.K. Rowling", "1997"),
            Triple("El Señor de los Anillos", "J.R.R. Tolkien", "1954")
        )

        // Elegir uno al azar
        val randomBook = samples.random()

        // Poner los textos en los campos
        etTitle.setText(randomBook.first)
        etAuthor.setText(randomBook.second)
        etYear.setText(randomBook.third)
    }

    // --- LÓGICA DE GUARDADO (MVC + SINGLETON) ---

    private fun saveBookToDatabase() {
        val title = etTitle.text.toString()
        val author = etAuthor.text.toString()
        val yearStr = etYear.text.toString()

        // 1. Validación simple
        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Crear el objeto Libro
        val newBook = Book(
            title = title,
            author = author,
            year = yearStr.toInt(),
            coverUri = currentPhotoPath // Aquí va la ruta de la foto (o null si no hizo foto)
        )

        // 3. Guardar en Room (Usando Corrutinas y Singleton)
        lifecycleScope.launch {
            // Llamada al Singleton como en los apuntes AD07
            AppSingleton.getInstance(this@NewBookActivity).database.bookDao().insertBook(newBook)

            Toast.makeText(this@NewBookActivity, "Libro guardado correctamente", Toast.LENGTH_SHORT).show()

            // 4. Cerrar y volver atrás
            finish()
        }
    }

    // Gestionar la respuesta del usuario al pedir permiso de cámara
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "El permiso de cámara es necesario para la foto", Toast.LENGTH_SHORT).show()
        }
    }
}