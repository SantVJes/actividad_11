@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.actividad_11.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.Database.Productos
import com.example.actividad_11.navegacion.Directorio
import com.example.actividad_11.navegacion.NavigationDrawerContent
import com.example.actividad_11.ui.theme.ProductosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modificar_inventario(navController: NavHostController, appContainer: AppContainer) {
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

                Body_Modificar(appContainer)

            }
        }
    }


}

@Composable
fun Body_Modificar(appContainer: AppContainer) {

    val viewModel = ProductosViewModel(appContainer.provideProductosRepositoy())

    // Estado para la entrada del usuario
    var userInput by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf<Productos?>(null) }

    // Estado local para almacenar los valores de los campos
    var productCode by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productCost by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var creado by remember { mutableStateOf(false) }
    // Búsqueda del producto por código
    val busqueda by remember(userInput) {
        viewModel.getProductoByCodigo(userInput.toIntOrNull() ?: 0)
    }.collectAsState(initial = null)

    // Si la búsqueda tiene un resultado, lo seleccionamos automáticamente
    LaunchedEffect(userInput) {
        // Esto se ejecuta cada vez que userInput cambia (después de la actualización del producto)
        busqueda?.let { selectedProduct = it }
    }

    Column(modifier = Modifier.padding(16.dp) .height(500.dp)) {
        // Campo de búsqueda
        TextField(
            value = userInput,
            onValueChange = { input -> userInput = input },
            label = { Text("Buscar producto a Modificar por Código") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje si el producto no existe
        if (userInput.isNotEmpty() && busqueda == null) {
            Text(
                text = "El producto con Código $userInput no existe",
                modifier = Modifier.padding(8.dp),
                color = Color.Red
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            //prudcto sleccionado
            busqueda?.let { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Código: ${producto.codigoProducto}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = "Descripción: ${producto.descripcion}", fontSize = 16.sp)
                        Text(text = "Existencias: ${producto.numeroExistencia}", fontSize = 16.sp)
                        Text(
                            text = "Costo: $${producto.costo}",
                            fontSize = 16.sp,
                            color = Color.Green
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Actualizar Descripción") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productCost,
                onValueChange = { productCost = it },
                label = { Text("Actualizar Costo") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productStock,
                onValueChange = { productStock = it },
                label = { Text("Actualizar Existencias") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
        if (creado) {
            Text(
                text = "Se Actualizo el producto",
                color = Color.Blue,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Ocultar el mensaje después de 2 segundos
            LaunchedEffect(Unit) {
                delay(2000) // 2000 milisegundos = 2 segundos
                creado = false
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
        // Botón para eliminar el producto seleccionado
        Button(
            onClick = {
                val code = userInput.toIntOrNull()
                if (productDescription.isEmpty()) {
                    productDescription = busqueda?.descripcion ?: ""
                }
                if (productCost.isEmpty()) {
                    productCost = busqueda?.costo?.toString() ?: ""
                }
                if (productStock.isEmpty()) {
                    productStock = busqueda?.numeroExistencia?.toString() ?: ""
                }


                val cost = productCost.toDoubleOrNull()
                val stock = productStock.toIntOrNull()

                if (code != null && stock != null && cost != null) {
                    creado = true
                    viewModel.updateProducto(Productos(code, productDescription, stock, cost))
                    userInput = ""
                }


            },
            modifier = Modifier .width(250.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Actualizar Producto", color = Color.White)

        }



        }





    }










}


@Preview(showBackground = true)
@Composable
fun BodyPreview_Modificar() {
    val appContainer = AppContainer(LocalContext.current)
    Modificar_inventario(navController = NavHostController(LocalContext.current), appContainer)

}

