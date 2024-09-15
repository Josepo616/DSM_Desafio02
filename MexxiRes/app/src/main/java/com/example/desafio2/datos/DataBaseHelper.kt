package com.example.desafio2.datos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.util.Log


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "desafio2.db"
        private const val DATABASE_VERSION = 2

        // Tablas y columnas de usuarios
        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Tablas y columnas de comidas
        private const val TABLE_COMIDAS = "comidas"
        private const val COLUMN_COMIDA_NAME = "name"
        private const val COLUMN_COMIDA_PRICE = "price"
        private const val COLUMN_COMIDA_IMAGE = "image"

        // Tablas y columnas de bebidas
        private const val TABLE_BEBIDAS = "bebidas"
        private const val COLUMN_BEBIDA_NAME = "name"
        private const val COLUMN_BEBIDA_PRICE = "price"
        private const val COLUMN_BEBIDA_IMAGE = "image"

        // Tablas y columnas de historial de compras
        private const val TABLE_HISTORIAL = "historial"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE_ITEMS = "nombre_items"
        private const val COLUMN_PRECIO_ITEMS = "precio_items"
        private const val COLUMN_TOTAL_ITEMS = "total_items"
        private const val COLUMN_TOTAL_PRECIO = "total_precio"
        private const val COLUMN_HORA_COMPRA = "hora_compra"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de usuarios
        val createTableUsers = ("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT)")
        db.execSQL(createTableUsers)

        val createTableComidas = ("CREATE TABLE " + TABLE_COMIDAS + " (" +
                COLUMN_COMIDA_NAME + " TEXT," +
                COLUMN_COMIDA_PRICE + " REAL," +
                COLUMN_COMIDA_IMAGE + " TEXT)")
        db.execSQL(createTableComidas)

        val createTableBebidas = ("CREATE TABLE " + TABLE_BEBIDAS + " (" +
                COLUMN_BEBIDA_NAME + " TEXT," +
                COLUMN_BEBIDA_PRICE + " REAL," +
                COLUMN_BEBIDA_IMAGE + " TEXT)")
        db.execSQL(createTableBebidas)

        // Crear tabla de historial
        val createTableHistorial = ("CREATE TABLE " + TABLE_HISTORIAL + " (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOMBRE_ITEMS TEXT," +
                "$COLUMN_PRECIO_ITEMS TEXT," +
                "$COLUMN_TOTAL_ITEMS INTEGER," +
                "$COLUMN_TOTAL_PRECIO REAL," +
                "$COLUMN_HORA_COMPRA TEXT)")
        db.execSQL(createTableHistorial)


        // Inserta el usuario por defecto ('admin', 'admin')
        val insertAdmin = "INSERT INTO $TABLE_USERS ($COLUMN_USERNAME, $COLUMN_PASSWORD) VALUES ('admin', 'admin')"
        db.execSQL(insertAdmin)

        // Inserta datos en la tabla de comidas
        insertComida(db, "Torta Mexicana", 3.00, "tortas")
        insertComida(db, "Tacos de birria", 5.00, "tacos")
        insertComida(db, "Sopa de tortilla", 4.00, "sopa")
        insertComida(db, "Burritos", 6.00, "burrito")
        insertComida(db, "Quesadillas", 4.00, "quesadillas")
        insertComida(db, "Pozole", 5.00, "pozole")
        insertComida(db, "Nachos", 4.00, "nachos")

        // Inserta datos en la tabla de bebidas
        insertBebida(db, "Horchata", 1.25, "horchata")
        insertBebida(db, "Jamaica", 1.25, "jamaica")
        insertBebida(db, "Limonada", 2.00, "limonada")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMIDAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BEBIDAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
        onCreate(db)
    }

    private fun insertComida(db: SQLiteDatabase, name: String, price: Double, image: String) {
        val values = ContentValues().apply {
            put(COLUMN_COMIDA_NAME, name)
            put(COLUMN_COMIDA_PRICE, price)
            put(COLUMN_COMIDA_IMAGE, image)
        }
        db.insert(TABLE_COMIDAS, null, values)
    }

    private fun insertBebida(db: SQLiteDatabase, name: String, price: Double, image: String) {
        val values = ContentValues().apply {
            put(COLUMN_BEBIDA_NAME, name)
            put(COLUMN_BEBIDA_PRICE, price)
            put(COLUMN_BEBIDA_IMAGE, image)
        }
        db.insert(TABLE_BEBIDAS, null, values)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Método para obtener la lista de comidas
    fun getComidas(): List<Comida> {
        val comidas = mutableListOf<Comida>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_COMIDAS,
            arrayOf(COLUMN_COMIDA_NAME, COLUMN_COMIDA_PRICE, COLUMN_COMIDA_IMAGE),
            null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_COMIDA_NAME))
            val price = cursor.getString(cursor.getColumnIndex(COLUMN_COMIDA_PRICE))
            val image = cursor.getString(cursor.getColumnIndex(COLUMN_COMIDA_IMAGE))
            comidas.add(Comida(name, price, image))
        }
        cursor.close()
        return comidas
    }

    fun getBebidas(): List<Bebida> {
        val bebidas = mutableListOf<Bebida>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_BEBIDAS,
            arrayOf(COLUMN_BEBIDA_NAME, COLUMN_BEBIDA_PRICE, COLUMN_BEBIDA_IMAGE),
            null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_BEBIDA_NAME))
            val price = cursor.getString(cursor.getColumnIndex(COLUMN_BEBIDA_PRICE))
            val image = cursor.getString(cursor.getColumnIndex(COLUMN_BEBIDA_IMAGE))
            bebidas.add(Bebida(name, price, image))
        }
        cursor.close()
        return bebidas
    }

    // Métodos para insertar en historial
    fun insertHistorial(nombreItems: String, precioItems: String, totalItems: Int, totalPrecio: Double, horaCompra: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_ITEMS, nombreItems)
            put(COLUMN_PRECIO_ITEMS, precioItems)
            put(COLUMN_TOTAL_ITEMS, totalItems)
            put(COLUMN_TOTAL_PRECIO, totalPrecio)
            put(COLUMN_HORA_COMPRA, horaCompra)
        }
        val result = db.insert(TABLE_HISTORIAL, null, values)
        if (result == -1L) {
            Log.e("DatabaseHelper", "Error inserting data into historial")
        } else {
            Log.d("DatabaseHelper", "Data inserted into historial with id $result")
        }
    }


    // Métodos para obtener el historial
    fun getHistorial(): List<Historial> {
        val historialList = mutableListOf<Historial>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_HISTORIAL,
            arrayOf(COLUMN_ID, COLUMN_NOMBRE_ITEMS, COLUMN_PRECIO_ITEMS, COLUMN_TOTAL_ITEMS, COLUMN_TOTAL_PRECIO, COLUMN_HORA_COMPRA),
            null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val nombreItems = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE_ITEMS))
            val precioItems = cursor.getString(cursor.getColumnIndex(COLUMN_PRECIO_ITEMS))
            val totalItems = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_ITEMS))
            val totalPrecio = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_PRECIO))
            val horaCompra = cursor.getString(cursor.getColumnIndex(COLUMN_HORA_COMPRA))

            // Convertir las cadenas de nombres y precios en listas de Item
            val nombresList = nombreItems.split(",")  // Suponemos que los nombres están separados por comas
            val preciosList = precioItems.split(",").map { it.toDouble() }  // Lo mismo con los precios, convirtiéndolos a Double

            // Crear la lista de ítems
            val itemsList = nombresList.zip(preciosList) { nombre, precio -> Item(nombre, precio) }

            // Añadir el historial a la lista
            historialList.add(Historial(id, itemsList, totalItems, totalPrecio, horaCompra))
        }
        cursor.close()
        return historialList
    }

    // Método para eliminar un registro por ID
    fun deleteHistorialRecord(id: Long): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_HISTORIAL, "id = ?", arrayOf(id.toString()))
        return rowsAffected > 0
    }

}