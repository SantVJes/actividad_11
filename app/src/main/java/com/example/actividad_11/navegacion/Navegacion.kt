package com.example.actividad_11.navegacion


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.pantallas.Eliminar_inventario
import com.example.actividad_11.pantallas.Modificar_Agregar
import com.example.actividad_11.pantallas.Modificar_Buscar
import com.example.actividad_11.pantallas.Modificar_inventario

import com.example.actividad_11.pantallas.Principal_inventario
import com.example.actividad_11.pantallas.Registrar_inventario


@Composable
fun AppNavigations(appContainer: AppContainer) {
    val navController = rememberNavController()

    // Obtén el contexto de la aplicación
    val holas = null
    NavHost(
        navController = navController, startDestination = Directorio.principal
    ) {

        composable(route = Directorio.principal) {

            Principal_inventario(navController,appContainer)
        }


        composable(route = Directorio.buscar) {

            Modificar_Buscar(navController,appContainer)
        }
        composable(route = Directorio.agregar) {

            Modificar_Agregar(navController,appContainer)
        }
        composable(route = Directorio.eliminar) {

            Eliminar_inventario(navController,appContainer)
        }
        composable(route = Directorio.modificar) {
            Modificar_inventario(navController,appContainer)

        }
        composable(route = Directorio.registrar) {
            Registrar_inventario(navController,appContainer)
        }

    }
}