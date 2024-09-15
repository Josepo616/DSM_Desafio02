package com.example.desafio2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.desafio2.datos.CarritoItem


class CarritoAdapter(
    context: Context,
    private val carrito: MutableList<CarritoItem>,
    private val onItemEliminado: () -> Unit // Interfaz para notificar la eliminación
) : ArrayAdapter<CarritoItem>(context, R.layout.item_carrito, carrito) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_carrito, parent, false)

        val itemNameTextView: TextView = view.findViewById(R.id.item_name)
        val itemPriceTextView: TextView = view.findViewById(R.id.item_price)
        val eliminarButton: Button = view.findViewById(R.id.eliminarButton)

        val item = carrito[position]

        itemNameTextView.text = item.nombre
        itemPriceTextView.text = "$" + item.precio

        // Dentro de CarritoAdapter, en el setOnClickListener del eliminarButton
        eliminarButton.setOnClickListener {
            carrito.removeAt(position)
            notifyDataSetChanged()
            (context as CarritoActivity).onItemEliminado() // Llamar a la función en la actividad
            onItemEliminado()

        }


        return view
    }
}
