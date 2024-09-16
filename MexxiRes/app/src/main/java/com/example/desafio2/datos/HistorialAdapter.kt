package com.example.desafio2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.datos.DataBaseHelper
import com.example.desafio2.datos.Historial
import java.util.Locale

class HistorialAdapter(private val historialList: List<Historial>, private val context: Context) :
    RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val historial = historialList[position]

        // Asignar la hora de compra
        holder.horaCompraTextView.text = "Hora del pedido: ${historial.horaCompra}"

        // Limpiar el layout de ítems por si ya tiene vistas previas
        holder.itemsLayout.removeAllViews()

        // Iterar sobre los ítems y precios y agregarlos dinámicamente
        historial.items.forEach { item ->
            // Crear TextView para el nombre del ítem
            val itemTextView = TextView(holder.itemView.context)
            itemTextView.text = item.nombre
            itemTextView.textSize = 14f
            itemTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemsLayout.addView(itemTextView)

            // Crear TextView para el precio del ítem
            val precioTextView = TextView(holder.itemView.context)
            precioTextView.text = "$${formatearPrecio(item.precio)}" // Formatear el precio aquí
            precioTextView.textSize = 14f
            precioTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemsLayout.addView(precioTextView)
        }

        // Asignar el total de ítems y precio
        holder.totalItemsTextView.text = "Total de Productos: ${historial.totalItems}"
        holder.totalPrecioTextView.text = "Precio total: $${formatearPrecio(historial.totalPrecio)}" // Formatear el total aquí

        // Configurar el botón de eliminar
        holder.eliminarButton.setOnClickListener {
            eliminarRegistro(historial.id)
        }
    }
    // Función para formatear un número a 2 decimales utilizando Locale.US
    private fun formatearPrecio(precio: Double): String {
        return String.format(Locale.US, "%.2f", precio)
    }

    override fun getItemCount(): Int {
        return historialList.size
    }

    private fun eliminarRegistro(id: Long) {
        val dbHelper = DataBaseHelper(context)
        val eliminado = dbHelper.deleteHistorialRecord(id)
        if (eliminado) {
            // Actualiza la lista después de eliminar el registro
            val newHistorialList = dbHelper.getHistorial()
            (context as HistorialActivity).updateHistorialList(newHistorialList)
            Toast.makeText(context, "Registro de compra eliminado exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            // Maneja el error en caso de que no se haya eliminado el registro
            Toast.makeText(context, "Error al eliminar el registro", Toast.LENGTH_SHORT).show()
        }
    }

    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val horaCompraTextView: TextView = itemView.findViewById(R.id.horaCompraTextView)
        val itemsLayout: LinearLayout = itemView.findViewById(R.id.itemsLayout)  // Donde se colocan los ítems
        val totalItemsTextView: TextView = itemView.findViewById(R.id.totalItemsTextView)
        val totalPrecioTextView: TextView = itemView.findViewById(R.id.totalPrecioTextView)
        val eliminarButton: Button = itemView.findViewById(R.id.eliminarButton)
    }
}
