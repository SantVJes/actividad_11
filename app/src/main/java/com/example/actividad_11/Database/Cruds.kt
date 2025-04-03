package com.example.actividad_11.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDao {

    // Insertar un nuevo producto yea
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducto(producto: Productos)

    @Query("SELECT * FROM productos WHERE codigoProducto = :codigo")
    suspend fun getProductoByCodigo(codigo: Int): Productos?
    // Buscar un producto por código yea

    @Query("SELECT * FROM productos WHERE descripcion = :descripcion")
    suspend fun getProductoByDescripcion(descripcion: String): Productos?


    // Actualizar un producto yea
    @Update
    suspend fun updateProducto(producto: Productos)

    // Eliminar un producto yea
    @Delete
    suspend fun deleteProducto(producto: Productos)

    // Obtener todos los productos yea
    @Query("SELECT * FROM productos")
    suspend fun getAllProductos(): List<Productos>
}

@Dao
interface MovimientoDao {

    // Insertar un nuevo movimiento (entrada o salida) yea
    @Insert
    suspend fun insertMovimiento(movimiento: Movimientos)


    // Obtener todos los movimientos yea
    @Query("SELECT * FROM movimientos")
    suspend fun getAllMovimientos(): List<Movimientos>

    // Obtener los movimientos de un producto específico  yea
    @Query("SELECT * FROM movimientos WHERE codigoProducto = :codigo")
    suspend fun getMovimientosByProducto(codigo: Int): List<Movimientos>
}


