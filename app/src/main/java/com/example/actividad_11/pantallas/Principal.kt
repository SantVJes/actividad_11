package com.example.actividad_11.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.Database.Productos
import com.example.actividad_11.navegacion.Directorio
import com.example.actividad_11.navegacion.NavigationDrawerContent
import com.example.actividad_11.ui.theme.ProductosViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal_inventario(navController: NavHostController, appContainer: AppContainer) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(navController , drawerState)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color(0xFF79D5E1),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Inventario de Productos",
                                style = TextStyle(
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xFF87CEEB),
                    contentColor = Color.Black
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Creado Por Jesus Santiago Velasco ",
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                Body_principal(navController, appContainer)
            }
        }
    }


}



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Body_principal(navController: NavHostController, appContainer: AppContainer) {

    Spacer(modifier = Modifier.height(16.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), // Espaciado superior e inferior
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Menú de Acciones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold, // Hace que el título resalte más
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
    val botones = listOf(
        "Buscar Un Producto" to { navController.navigate(Directorio.buscar) },
        "Agregar Un Producto" to { navController.navigate(Directorio.agregar)},
        "Eliminar Un Producto" to {navController.navigate(Directorio.eliminar)},
        "Modificar Un Producto" to { navController.navigate(Directorio.modificar) },
        "Registrar Un Movimiento" to { navController.navigate(Directorio.registrar)}
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), // Espaciado superior e inferior
        contentAlignment = Alignment.Center
    ) {
        Column {
            botones.forEach { (texto, accion) ->
                ElevatedButton(
                    onClick = accion,
                    modifier = Modifier
                        .width(250.dp)
                        .padding(vertical = 15.dp) // Espaciado entre botones
                ) {
                    Text(texto, fontSize = 18.sp)
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun Body_Preview() {

    val appContainer = AppContainer(LocalContext.current)
    val navController = rememberNavController()
    Principal_inventario(navController , appContainer)


}


