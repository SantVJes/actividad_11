package com.example.actividad_11.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Productos::class, Movimientos::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, InventoryDatabase::class.java, "inventory_database")
                    .addCallback(DatabaseCallback()) // Llamar al Callback
                    .build()
                    .also { instance = it }
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val dao = instance?.productoDao()
                dao?.insertProducto(Productos(1, "Laptop ASUS", 10, 15000.0))
                dao?.insertProducto(Productos(2, "Mouse Logitech", 50, 500.0))
                dao?.insertProducto(Productos(3, "Teclado Mecánico", 30, 1200.0))
            }
        }
    }
}

