package com.example.actividad_11.Database

import android.content.Context
import androidx.room.Room

class AppContainer (context: Context) {

    private  val inventoryDatabase = InventoryDatabase.getDatabase(context)
    private  val productosDao = inventoryDatabase.productoDao()
    private  val movimientoDao = inventoryDatabase.movimientoDao()
    private  val productosRepositoy = ProductosRepositoryImple(productosDao)
    private  val movimientoRepositoy = MovimientoRepositoryImple(movimientoDao)

    fun provideProductosRepositoy(): ProductosRepository {

        return  productosRepositoy
    }

    fun provideMovimientoRepositoy(): MovimientosRepository {

        return movimientoRepositoy
    }


}