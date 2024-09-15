package com.example.desafio2.datos

data class Historial(
    val id: Long,
    val items: List<Item>,   // Lista de ítems
    val totalItems: Int,
    val totalPrecio: Double,
    val horaCompra: String
)

data class Item(
    val nombre: String,
    val precio: Double
)
