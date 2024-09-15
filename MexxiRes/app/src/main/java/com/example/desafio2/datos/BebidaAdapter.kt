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
import com.example.desafio2.datos.Bebida

class BebidaAdapter(
    private val context: Context,
    private val bebidas: List<Bebida>,
    private val onAddToCartClick: (Bebida) -> Unit,
    private val formatearPrecio: (String) -> String // Acepta la función como parámetro

) : RecyclerView.Adapter<BebidaAdapter.BebidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BebidaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bebida_card, parent, false)
        return BebidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BebidaViewHolder, position: Int) {
        val bebida = bebidas[position]
        holder.nameTextView.text = bebida.name
        holder.priceTextView.text = "$" + formatearPrecio(bebida.price) // Aplica formato al precio
        // Cargar imagen usando BitmapFactory
        val imageId = context.resources.getIdentifier(bebida.image, "drawable", context.packageName)
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageId)
        holder.imageView.setImageBitmap(bitmap)

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(bebida)
        }
    }

    override fun getItemCount(): Int = bebidas.size

    inner class BebidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bebida_image)
        val nameTextView: TextView = itemView.findViewById(R.id.bebida_name)
        val priceTextView: TextView = itemView.findViewById(R.id.bebida_price)
        val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)
    }
}
