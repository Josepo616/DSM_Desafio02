package com.example.desafio2

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.datos.DataBaseHelper
import com.example.desafio2.datos.Historial

class HistorialActivity : AppCompatActivity() {

    private lateinit var historialRecyclerView: RecyclerView
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        historialRecyclerView = findViewById(R.id.historialRecyclerView)
        historialRecyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(historialRecyclerView.context, LinearLayoutManager.VERTICAL)
        historialRecyclerView.addItemDecoration(dividerItemDecoration)

        // Cargar historial de la base de datos
        val dbHelper = DataBaseHelper(this)
        val historialList = dbHelper.getHistorial()

        // Configurar el adaptador
        historialAdapter = HistorialAdapter(historialList, this)
        historialRecyclerView.adapter = historialAdapter

        // Mostrar mensaje si el historial está vacío
        if (historialList.isEmpty()) {
            findViewById<TextView>(R.id.empty_message).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.empty_message).visibility = View.GONE
        }
    }

    fun updateHistorialList(newList: List<Historial>) {
        historialAdapter = HistorialAdapter(newList, this)
        historialRecyclerView.adapter = historialAdapter
    }
}
