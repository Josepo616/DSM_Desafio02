package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.datos.DataBaseHelper
import com.example.desafio2.datos.Comida
import com.example.desafio2.datos.Bebida
import com.example.desafio2.datos.BebidaAdapter
import com.example.desafio2.datos.CarritoItem
import com.example.desafio2.datos.ComidaAdapter
import java.util.Locale

class MenuActivity : AppCompatActivity() {

    private val carrito = mutableListOf<CarritoItem>()
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var comidasRecyclerView: RecyclerView
    private lateinit var bebidasRecyclerView: RecyclerView
    private lateinit var comidasAdapter: ComidaAdapter
    private lateinit var bebidasAdapter: BebidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        dbHelper = DataBaseHelper(this)

        val verCarritoBtn = findViewById<Button>(R.id.verCarritoButton)
        verCarritoBtn.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putParcelableArrayListExtra("carrito", ArrayList(carrito))
            startActivityForResult(intent, REQUEST_CODE_CARRITO)
        }

        val verHistorialButton = findViewById<Button>(R.id.verHistorialButton)
        verHistorialButton.setOnClickListener {
            try {
                Log.d("Historial", "Botón de ver historial presionado. Iniciando HistorialActivity.")
                val intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("Historial", "Error al iniciar HistorialActivity", e)
                Toast.makeText(this, "Ocurrió un error al abrir el historial", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar RecyclerView para comidas
        comidasRecyclerView = findViewById(R.id.comidasContainer)
        comidasRecyclerView.layoutManager = LinearLayoutManager(this)
        comidasAdapter = ComidaAdapter(this, listOf(), { comida ->
            agregarAlCarrito(comida.name, comida.price)
        }, ::formatearPrecio) // Pasar la función formatearPrecio
        comidasRecyclerView.adapter = comidasAdapter

        // Configurar RecyclerView para bebidas
        bebidasRecyclerView = findViewById(R.id.bebidasContainer)
        bebidasRecyclerView.layoutManager = LinearLayoutManager(this)
        bebidasAdapter = BebidaAdapter(this, listOf(), { bebida ->
            agregarAlCarrito(bebida.name, bebida.price)
        }, ::formatearPrecio) // Pasar la función formatearPrecio
        bebidasRecyclerView.adapter = bebidasAdapter

        cargarDatos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CARRITO && resultCode == RESULT_OK) {
            val carritoActualizado = data?.getParcelableArrayListExtra<CarritoItem>("carritoActualizado")
            carrito.clear()
            carritoActualizado?.let {
                carrito.addAll(it)
            }
            // Notificar a los adaptadores si es necesario
            comidasAdapter.notifyDataSetChanged()
            bebidasAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val REQUEST_CODE_CARRITO = 1
    }

    private fun cargarDatos() {
        val comidas = dbHelper.getComidas().map { comida ->
            comida.copy(price = formatearPrecio(comida.price))
        }
        val bebidas = dbHelper.getBebidas().map { bebida ->
            bebida.copy(price = formatearPrecio(bebida.price))
        }

        // Actualizar los adapters con los datos obtenidos
        comidasAdapter = ComidaAdapter(
            context = this,
            comidas = comidas,
            onAddToCartClick = { comida -> agregarAlCarrito(comida.name, comida.price) },
            formatearPrecio = ::formatearPrecio // Pasar la función formatearPrecio
        )
        comidasRecyclerView.adapter = comidasAdapter

        bebidasAdapter = BebidaAdapter(
            context = this,
            bebidas = bebidas,
            onAddToCartClick = { bebida -> agregarAlCarrito(bebida.name, bebida.price) },
            formatearPrecio = ::formatearPrecio // Pasar la función formatearPrecio
        )
        bebidasRecyclerView.adapter = bebidasAdapter
    }

    fun formatearPrecio(precioStr: String): String {
        return try {
            // Convertir el String a Double
            val precioDouble = precioStr.toDouble()
            // Formatear el precio a dos decimales
            String.format(Locale.US, "%.2f", precioDouble)
        } catch (e: NumberFormatException) {
            // Manejar el caso donde el String no puede ser convertido a Double
            "0.00"
        }
    }

    private fun agregarAlCarrito(nombre: String, precio: String) {
        val precioFormateado = formatearPrecio(precio)
        val item = CarritoItem(nombre, precioFormateado)
        carrito.add(item)
        Toast.makeText(this, "${item.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
    }
}
