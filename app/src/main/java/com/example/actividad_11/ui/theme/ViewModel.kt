package com.example.actividad_11.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad_11.Database.Movimientos
import com.example.actividad_11.Database.MovimientosRepository
import com.example.actividad_11.Database.Productos
import com.example.actividad_11.Database.ProductosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(private val repository: ProductosRepository) : ViewModel() {
    val productos: Flow<List<Productos>> = repository.getAllProductosStream()

    fun insertProducto(producto: Productos) {
        viewModelScope.launch {
            repository.insertProducto(producto)
        }
    }

    fun deleteProducto(producto: Productos) {
        viewModelScope.launch {
            repository.deleteProducto(producto)
        }
    }

    fun updateProducto(producto: Productos) {
        viewModelScope.launch {
            repository.updateProducto(producto)
        }
    }



    // Función para obtener el producto por código
     fun getProductoByCodigo(codigo: Int ): Flow<Productos?> {
        return if (codigo != 0) {
            repository.getProductoStream(codigo) // Realiza la búsqueda si el código es válido
        } else {
            flowOf(null) // Si el código es 0, retorna null
        }
    }

    // Función para obtener el producto por Descripcion
    fun getProductoBydescripcion(descripcion: String ): Flow<Productos?> {
        return if (descripcion != "") {
            repository.getProductoStream(descripcion) // Realiza la búsqueda si el código es válido
        } else {
            flowOf(null) // Si el código es 0, retorna null
        }
    }


}

class MovimientosViewModel(private val repository: MovimientosRepository) : ViewModel() {
    val movimientos: Flow<List<Movimientos>> = repository.getAllMovimientosStream()

    fun insertMovimiento(movimiento: Movimientos) {
        viewModelScope.launch {
            repository.insertMovimiento(movimiento)
        }
    }
}