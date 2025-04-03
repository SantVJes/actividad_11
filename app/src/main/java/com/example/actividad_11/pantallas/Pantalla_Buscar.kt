@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.actividad_11.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.draw.scale
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.Database.Productos
import com.example.actividad_11.navegacion.Directorio
import com.example.actividad_11.navegacion.NavigationDrawerContent
import com.example.actividad_11.ui.theme.ProductosViewModel
import kotlinx.coroutines.launch

@Composable
fun Modificar_Buscar(navController: NavHostController, appContainer: AppContainer) {
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
            }, floatingActionButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomStart // Alineación a la izquierda
                ) {
                    ExtendedFloatingActionButton(
                        onClick = { navController.navigate(Directorio.principal) },
                        text = { Text(text = "Regresar") },
                        icon = { Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar") },
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp,)
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                Body_Buscar( appContainer)
            }
        }
    }


}

@Composable
fun Body_Buscar( appContainer: AppContainer) {
    // Obtén el ViewModel
    val viewModel = ProductosViewModel(appContainer.provideProductosRepositoy())
    // Estado para la entrada del usuario
    var userInput by remember { mutableStateOf("") }

    // Lista de todos los productos
    val allProducts by viewModel.productos.collectAsState(emptyList())

    var seleccion = "";
    val checkedState = remember { mutableStateOf(true) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = if (checkedState.value) "Buscar por descripción" else "Buscar por código",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )

        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            modifier = Modifier
                .padding(8.dp)
                .scale(1.2f), // Aumenta el tamaño del Switch
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50), // Verde elegante cuando está activado
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFBDBDBD) // Gris cuando está desactivado
            )
        )
    }


    val busqueda by remember(userInput, checkedState.value) {
        if (!checkedState.value) {
            seleccion = "codijo";
            viewModel.getProductoByCodigo(userInput.toIntOrNull() ?: 0)
        } else {
            seleccion = "descripcion";
            viewModel.getProductoBydescripcion(userInput)
        }
    }.collectAsState(initial = null)







    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = userInput,
            onValueChange = { input -> userInput = input },
            label = { Text("Buscar producto por $seleccion") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (userInput.isNotEmpty() && busqueda == null) {
            // Si se ha ingresado un código pero no se encontró el producto
            Text(
                text = "El producto con $seleccion $userInput no existe",
                modifier = Modifier.padding(8.dp),
                color = Color.Red
            )


        }

        // Mostrar el producto buscado si existe
        busqueda?.let {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Código: ${it.codigoProducto}", fontWeight = FontWeight.Bold)
                Text(text = "Descripción: ${it.descripcion}")
                Text(text = "Existencias: ${it.numeroExistencia}")
                Text(text = "Costo: $${it.costo}")
            }
        } ?: run {
            // Si no se encontró, mostrar todos los productos
            LazyColumn {
                items(allProducts) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),

                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Código: ${producto.codigoProducto}", fontWeight = FontWeight.Bold)
                            Text(text = "Descripción: ${producto.descripcion}")
                            Text(text = "Existencias: ${producto.numeroExistencia}")
                            Text(text = "Costo: $${producto.costo}")
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BodyPreview_Buscar() {
    val appContainer = AppContainer(LocalContext.current)
    val navController = rememberNavController()
    Modificar_Buscar(navController , appContainer)

}

