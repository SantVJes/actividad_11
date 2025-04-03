@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.actividad_11.pantallas

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
fun Modificar_Agregar(navController: NavHostController, appContainer: AppContainer) {



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
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp , )
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                Body_Agregar(appContainer)

            }
        }
    }




}
@Composable
fun Body_Agregar( appContainer: AppContainer) {
    // Obtén el ViewModel
    val viewModel = ProductosViewModel(appContainer.provideProductosRepositoy())

    // Estado local para almacenar los valores de los campos
    var productCode by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productCost by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }  // Estado para el mensaje de error
    var creado by remember { mutableStateOf(false) }
    // Usar un estado que se actualiza con el resultado de la búsqueda del producto
    val existingProduct = viewModel.getProductoByCodigo(productCode.toIntOrNull() ?: 0).collectAsState(initial = null)



    Spacer(modifier = Modifier.height(16.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), // Espaciado superior e inferior
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Incerte la informacion del Producto Nuevo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold, // Hace que el título resalte más
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Campos de entrada
        OutlinedTextField(
            value = productCode,
            onValueChange = { productCode = it },
            label = { Text("Código del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productCost,
            onValueChange = { productCost = it },
            label = { Text("Costo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = productStock,
            onValueChange = { productStock = it },
            label = { Text("Existencias") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Mensaje de error si el producto ya existe
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (creado) {
            Text(
                text = "Se agregó el producto",
                color = Color.Blue,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Ocultar el mensaje después de 2 segundos
            LaunchedEffect(Unit) {
                delay(2000) // 2000 milisegundos = 2 segundos
                creado = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Espaciado superior e inferior
            contentAlignment = Alignment.Center)
        {
        // Botón para agregar el producto
        FilledTonalButton(
            modifier = Modifier.width(250.dp),
            onClick = {
                // Validar si los campos no están vacíos y son correctos
                if (productCode.isNotEmpty() && productDescription.isNotEmpty() &&
                    productCost.isNotEmpty() && productStock.isNotEmpty()) {

                    // Intentamos convertir los valores de texto a los tipos esperados
                    val code = productCode.toIntOrNull()
                    val cost = productCost.toDoubleOrNull()
                    val stock = productStock.toIntOrNull()

                    if (code != null && cost != null && stock != null) {
                        // Verificar si el producto ya existe
                        if (existingProduct.value != null) {
                            // Si el producto existe, mostramos el mensaje de error
                            errorMessage = "El producto con código $code ya existe"
                        } else {
                            // Si no existe, insertamos el producto
                            viewModel.insertProducto(Productos(code, productDescription, stock ,cost))
                            // Limpiar campos y el mensaje de error después de la inserción
                            creado = true;
                            productCode = ""
                            productDescription = ""
                            productCost = ""
                            productStock = ""
                            errorMessage = ""

                        }
                    }
                }
            }
        ) {
            Text(text = "Agregar")
        }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun BodyPreview_Agregar() {
  val appContainer = AppContainer(LocalContext.current)

  Modificar_Agregar(navController = NavHostController(LocalContext.current), appContainer)
}
