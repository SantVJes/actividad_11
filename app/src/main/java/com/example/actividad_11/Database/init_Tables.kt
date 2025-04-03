package com.example.actividad_11.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "productos")
data class Productos(
    @PrimaryKey val codigoProducto: Int,
    val descripcion: String,
    val numeroExistencia: Int,
    val costo: Double
)

@Entity(
    tableName = "movimientos",
    foreignKeys = [
        ForeignKey(
            entity = Productos::class,
            parentColumns = ["codigoProducto"],
            childColumns = ["codigoProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Movimientos(
    @PrimaryKey(autoGenerate = true) val numMovimiento: Int = 0,
    val codigoProducto: Int,
    val tipoMovimiento: String, // "entrada" o "salida"
    val fecha: String,
    val cantidad: Int
)

