package com.example.desafio2.datos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.R
import com.example.desafio2.datos.Comida

class ComidaAdapter(
    private val context: Context,
    private val comidas: List<Comida>,
    private val onAddToCartClick: (Comida) -> Unit,
    private val formatearPrecio: (String) -> String // Acepta la función como parámetro
) : RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comida_card, parent, false)
        return ComidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = comidas[position]
        holder.nameTextView.text = comida.name
        holder.priceTextView.text = "$" + formatearPrecio(comida.price) // Aplica formato al precio
        // Cargar imagen usando BitmapFactory
        val imageId = context.resources.getIdentifier(comida.image, "drawable", context.packageName)
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageId)
        holder.imageView.setImageBitmap(bitmap)

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(comida)
        }
    }

    override fun getItemCount(): Int = comidas.size

    inner class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.comida_image)
        val nameTextView: TextView = itemView.findViewById(R.id.comida_name)
        val priceTextView: TextView = itemView.findViewById(R.id.comida_price)
        val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)
    }
}

