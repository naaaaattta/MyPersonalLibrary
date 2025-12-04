package com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dam202526_0489_2.proyecto1melgarejo_natalia_meza_joel.MainActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // 1. Configurar botón ENTRAR
        val btnEnter = findViewById<Button>(R.id.btnEnter)
        btnEnter.setOnClickListener {
            // Navegar a la pantalla principal (la lista de libros)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 2. Configurar botón SALIR
        val btnExit = findViewById<Button>(R.id.btnExit)
        btnExit.setOnClickListener {
            finishAffinity() // Cierra toda la aplicación
        }
    }
}