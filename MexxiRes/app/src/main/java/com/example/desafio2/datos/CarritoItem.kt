package com.example.desafio2.datos

import android.os.Parcel
import android.os.Parcelable

data class CarritoItem(
    val nombre: String,
    val precio: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(precio)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarritoItem> {
        override fun createFromParcel(parcel: Parcel): CarritoItem {
            return CarritoItem(parcel)
        }

        override fun newArray(size: Int): Array<CarritoItem?> {
            return arrayOfNulls(size)
        }
    }
}
