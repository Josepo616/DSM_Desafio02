package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2.datos.CarritoItem
import com.example.desafio2.datos.DataBaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CarritoActivity : AppCompatActivity() {


    val fechaHoraActual = Date()

    private lateinit var carritoListView: ListView
    private lateinit var realizarCompraButton: Button
    private lateinit var limpiarCarritoButton: Button
    private lateinit var preciototal: TextView
    private lateinit var itemtotal: TextView

    private val carrito = mutableListOf<CarritoItem>()
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_layout)

        carritoListView = findViewById(R.id.carritoListView)
        realizarCompraButton = findViewById(R.id.realizarCompraButton)
        limpiarCarritoButton = findViewById(R.id.limpiarCarritoButton)
        preciototal = findViewById(R.id.total_price)
        itemtotal = findViewById(R.id.total_items)

        // Obtener la lista de elementos pasados desde el intent
        carrito.addAll(intent.getParcelableArrayListExtra("carrito") ?: emptyList())

        // Configurar el adaptador
        adapter = CarritoAdapter(this, carrito) {
            calcularPrecioTotal()  // Recalcular el total cuando se elimine un ítem
            calcularTotalItems()
        }
        carritoListView.adapter = adapter


        // Calcular el total inicialmente
        calcularPrecioTotal()
        calcularTotalItems()

        // Configurar el botón "Realizar Compra"
        realizarCompraButton.setOnClickListener {
            realizarCompra()
        }

        // Configurar el botón "Limpiar Carrito"
        limpiarCarritoButton.setOnClickListener {
            limpiarCarrito()
        }
    }

    private fun realizarCompra() {
        try {
            if (carrito.isNotEmpty()) {
                // Unir los nombres de los items en el carrito
                val nombreItems = carrito.joinToString(", ") { it.nombre }

                // Limpiar y formatear los precios de cada item
                val precioItems = carrito.joinToString(", ") {
                    val precioLimpio = limpiarPrecio(it.precio.toString())
                    val precioDouble = precioLimpio.toDouble()
                    val precioFormateado = formatearPrecio(precioDouble)
                    Log.d("Compra", "Precio original: ${it.precio}, Precio limpio: $precioLimpio, Precio formateado: $precioFormateado")
                    precioFormateado
                }

                val totalItems = carrito.size
                // Calculamos el total sumando los precios originales
                val totalPrecio = carrito.sumOf { it.precio.toDouble() }

                // Formatear el total a dos decimales para mostrarlo
                val totalPrecioFormateado = formatearPrecio(totalPrecio)
                Log.d("Compra", "Total precio formateado: $totalPrecioFormateado")

                // Crear un formateador para la fecha
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaFormateada = formatoFecha.format(fechaHoraActual)

                // Crear un formateador para la hora
                val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val horaFormateada = formatoHora.format(fechaHoraActual)

                // Concatenar fecha y hora
                val fechaCompleta = "$fechaFormateada -- $horaFormateada"

                // Guardar en la base de datos con el valor numérico de totalPrecio
                val dbHelper = DataBaseHelper(this)
                dbHelper.insertHistorial(nombreItems, precioItems, totalItems, totalPrecio, fechaCompleta) // Aquí usamos totalPrecio, no totalPrecioFormateado
                Log.d("Compra", "Compra realizada: $nombreItems, Total: $totalPrecioFormateado, Hora: $fechaCompleta")

                Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()

                // Limpiar el carrito y notificar al adaptador
                carrito.clear()
                adapter.notifyDataSetChanged()

                // Devolver el carrito vacío a MenuActivity
                val resultIntent = Intent()
                resultIntent.putParcelableArrayListExtra("carritoActualizado", ArrayList(carrito))
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Log.w("Compra", "El carrito está vacío")
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("Compra", "Error al realizar la compra", e)
            Toast.makeText(this, "Ocurrió un error al realizar la compra", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para formatear un número a 2 decimales utilizando Locale.US
    private fun formatearPrecio(precio: Double): String {
        return String.format(Locale.US, "%.2f", precio)
    }

    // Función para limpiar caracteres no numéricos del precio
    private fun limpiarPrecio(precio: String): String {
        return precio.replace(Regex("[^\\d.,]"), "")
    }






    fun convertirPrecio(precioConFormato: String): Double {
        // Quitar el símbolo de dólar y posibles espacios en blanco
        val precioSinSimbolo = precioConFormato.replace("$", "").trim()
        return try {
            // Convertir la cadena a Double
            precioSinSimbolo.toDouble()
        } catch (e: NumberFormatException) {
            // Manejar el caso en que la conversión falla
            Log.e("CarritoTotal", "Error al convertir el precio a Double: $precioConFormato", e)
            0.0 // Devolver un valor por defecto o lanzar una excepción según sea necesario
        }
    }


    // Función para calcular el total del carrito
    private fun calcularPrecioTotal() {
        var total = 0.0
        for (item in carrito) {
            try {
                total += convertirPrecio(item.precio) // Usar la función convertirPrecio aquí
            } catch (e: NumberFormatException) {
                Log.e("CarritoTotal", "Error al convertir el precio a Double: ${item.precio}", e)
            }
        }
        preciototal.text = "Total: $%.2f".format(total)
    }

    private fun calcularTotalItems() {
        var total = 0
        for (item in carrito) {
            total += 1 // Contar cada ítem
        }
        // Actualizar el TextView con el total de ítems
        itemtotal.text = "Total items: $total"
    }


    private fun limpiarCarrito() {
        carrito.clear()
        adapter.notifyDataSetChanged()
        calcularPrecioTotal()  // Actualizar total cuando se vacíe el carrito
        calcularTotalItems()
        Toast.makeText(this, "Carrito limpiado", Toast.LENGTH_SHORT).show()

        // Devolver el carrito actualizado a MenuActivity
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra("carritoActualizado", ArrayList(carrito))
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    // Añade esta función a CarritoActivity
    fun onItemEliminado() {
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra("carritoActualizado", ArrayList(carrito))
        setResult(RESULT_OK, resultIntent)
        calcularTotalItems() // Actualiza el total de ítems después de eliminar
    }


}

