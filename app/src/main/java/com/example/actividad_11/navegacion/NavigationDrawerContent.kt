package com.example.actividad_11.navegacion

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


@Composable
fun NavigationDrawerContent(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val menuOpciones = mapOf(
        "Menu Principal" to { navController.navigate(Directorio.principal) },
        "Buscar Producto" to { navController.navigate(Directorio.buscar) },
        "Agregar Producto" to { navController.navigate(Directorio.agregar) },
        "Eliminar Producto" to { navController.navigate(Directorio.eliminar) },
        "Modificar Producto" to { navController.navigate(Directorio.modificar) },
        "Registrar Movimiento" to { navController.navigate(Directorio.registrar) }
    )

    ModalDrawerSheet (  modifier = Modifier.width(250.dp)){
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Menú",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        menuOpciones.forEach { (titulo, accion) ->
            DrawerItem(titulo, onClick = accion)
        }



        Divider()

        DrawerItem("Salir", onClick = {
            scope.launch { drawerState.close() }
            exitProcess(0)

        })

    }
}


@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text, fontSize = MaterialTheme.typography.titleMedium.fontSize)
    }
}