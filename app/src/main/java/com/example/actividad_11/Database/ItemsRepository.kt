package com.example.actividad_11.Database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Repositorio que proporciona inserción, actualización, eliminación y obtención de [Producto] y [Movimiento] desde una fuente de datos.
 */
interface ProductosRepository {
    /**
     * Obtener todos los productos desde la fuente de datos.
     */
    fun getAllProductosStream(): Flow<List<Productos>>

    /**
     * Obtener un producto desde la fuente de datos que coincida con el [codigoProducto].
     */
    fun getProductoStream(codigoProducto: Int): Flow<Productos?>

    /**
     * Obtener un producto desde la fuente de datos que coincida con el [descripcion].
     */
    fun getProductoStream(descripcion: String): Flow<Productos?>

    /**
     * Insertar un producto en la fuente de datos.
     */
    suspend fun insertProducto(producto: Productos)

    /**
     * Eliminar un producto de la fuente de datos.
     */
    suspend fun deleteProducto(producto: Productos)

    /**
     * Actualizar un producto en la fuente de datos.
     */
    suspend fun updateProducto(producto: Productos)

}

interface MovimientosRepository {
    /**
     * Obtener todos los movimientos desde la fuente de datos.
     */
    fun getAllMovimientosStream(): Flow<List<Movimientos>>

    /**
     * Obtener los movimientos de un producto desde la fuente de datos.
     */
    fun getMovimientosStream(codigoProducto: Int): Flow<List<Movimientos>>

    /**
     * Insertar un movimiento en la fuente de datos.
     */
    suspend fun insertMovimiento(movimiento: Movimientos)


}

class ProductosRepositoryImple(private val productoDao: ProductoDao) : ProductosRepository {
    override fun getAllProductosStream(): Flow<List<Productos>> = flow {
        emit(productoDao.getAllProductos())
    }

    override fun getProductoStream(codigoProducto: Int): Flow<Productos?> = flow {
        emit(productoDao.getProductoByCodigo(codigoProducto))
    }

    override fun getProductoStream(descripcion: String): Flow<Productos?> = flow {
        emit(productoDao.getProductoByDescripcion(descripcion))
    }

    override suspend fun insertProducto(producto: Productos) {
        productoDao.insertProducto(producto)
    }

    override suspend fun deleteProducto(producto: Productos) {
        productoDao.deleteProducto(producto)
    }

    override suspend fun updateProducto(producto: Productos) {
        productoDao.updateProducto(producto)
    }
}

class MovimientoRepositoryImple(private val movimientoDao: MovimientoDao) : MovimientosRepository {
    override fun getAllMovimientosStream(): Flow<List<Movimientos>> = flow {
        emit(movimientoDao.getAllMovimientos())
    }

    override fun getMovimientosStream(codigoProducto: Int): Flow<List<Movimientos>> = flow {
        emit(movimientoDao.getMovimientosByProducto(codigoProducto))
    }

    override suspend fun insertMovimiento(movimiento: Movimientos) {
        movimientoDao.insertMovimiento(movimiento)
    }
}