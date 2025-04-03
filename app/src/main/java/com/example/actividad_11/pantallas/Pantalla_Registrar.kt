@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.actividad_11.pantallas

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.draw.scale
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
import androidx.navigation.compose.rememberNavController
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.Database.Movimientos
import com.example.actividad_11.Database.Productos
import com.example.actividad_11.navegacion.Directorio
import com.example.actividad_11.navegacion.NavigationDrawerContent
import com.example.actividad_11.ui.theme.MovimientosViewModel
import com.example.actividad_11.ui.theme.ProductosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Registrar_inventario(navController: NavHostController, appContainer: AppContainer) {

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
                        modifier = Modifier.padding(start = 16.dp, bottom = 0.dp,)
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                Body_Registrar(appContainer)

            }
        }
    }


}

@Composable
fun Body_Registrar(appContainer: AppContainer) {
    var isDatePickerVisible by remember { mutableStateOf(false) } // Controla la visibilidad del DatePicker
    val viewModel = ProductosViewModel(appContainer.provideProductosRepositoy())
    val mviewModel = MovimientosViewModel(appContainer.provideMovimientoRepositoy())
    val datePickerState = rememberDatePickerState()
    var selectedProduct by remember { mutableStateOf<Productos?>(null) }
    var creado by remember { mutableStateOf(false) }

    val checkedState = remember { mutableStateOf(true) }
    var productCode by remember { mutableStateOf("") }
    var fechaRegistro by remember { mutableStateOf("") }
    var cantidadProduct by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var nuevoStock by remember { mutableStateOf(0) }
    var stock by remember {  mutableStateOf(0)  }
    var descripcion by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf(0.0) }
    // Búsqueda del producto por código
    val busqueda by remember(productCode) {
        viewModel.getProductoByCodigo(productCode.toIntOrNull() ?: 0)
    }.collectAsState(initial = null)

    // Si la búsqueda tiene un resultado, lo seleccionamos automáticamente
    LaunchedEffect(productCode) {
        // Esto se ejecuta cada vez que userInput cambia (después de la actualización del producto)
        busqueda?.let { selectedProduct = it }
    }
    // Si la búsqueda tiene un resultado, lo seleccionamos automáticamente

    // Row con el Switch para elegir entre Entrada y Salida
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = if (checkedState.value) "Registrar Salida" else "Registrar Entrada",
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
    // Mostrar el DatePicker si isDatePickerVisible es true
    if (isDatePickerVisible) {
        // Mostrar DatePicker
        Column(
            modifier = Modifier
                .wrapContentSize() // Limita el tamaño del Column
                .padding(20.dp), // Espaciado alrededor
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DatePicker(state = datePickerState)


            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre DatePicker y el botón

            // Hacer el botón más pequeño
            Button(
                onClick = {
                    val dateMillis = datePickerState.selectedDateMillis
                    if (dateMillis != null) {
                        // Formateamos la fecha seleccionada y la guardamos
                        val formattedDate = java.text.SimpleDateFormat("dd/MM/yyyy").format(dateMillis)
                        fechaRegistro = formattedDate
                        isDatePickerVisible = false // Cerrar el DatePicker después de seleccionar la fecha
                    }
                },
                modifier = Modifier .wrapContentSize(align =Alignment.Center) // Ajustar el tamaño del botón (ancho, alto)
            ) {
                Text("Confirmar Fecha")
            }
        }
    }


    if (checkedState.value) {  // Registrar Salida
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Registrar Salida",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(modifier = Modifier.padding(0.dp)) {
            //prudcto sleccionado
            busqueda?.let { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    stock = producto.numeroExistencia
                    descripcion = producto.descripcion
                    costo = producto.costo
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
                        )
                    }
                }
            }
        }




        Column(modifier = Modifier.padding(16.dp)) {
            // Campo de Código del Producto
            OutlinedTextField(
                value = productCode,
                onValueChange = { productCode = it },
                label = { Text("Código del producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            // Mensaje si el producto no existe
            if (productCode.isNotEmpty() && busqueda == null) {
                Text(
                    text = "El producto con Código $productCode no existe",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red
                )
            }






            Spacer(modifier = Modifier.height(8.dp))



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
                    .clickable { isDatePickerVisible = true } // Esto hará que al hacer clic en el campo se muestre el DatePicker
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(4.dp)) // Puedes agregar borde si lo deseas
            ) {
                Text(
                    text = if (fechaRegistro.isEmpty()) "Seleccionar Fecha" else fechaRegistro,
                    style = TextStyle(fontSize = 16.sp, color = if (fechaRegistro.isEmpty()) Color.DarkGray else Color.Black),
                    modifier = Modifier.padding(18.dp)
                )
            }




            Spacer(modifier = Modifier.height(8.dp))

            // Campo para Cantidad (puedes usar este campo como cantidad para la salida)
            OutlinedTextField(
                value = cantidadProduct,
                onValueChange = { cantidadProduct = it },
                label = { Text("Cantidad a retirar") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )



        }
    } else {  // Registrar Entrada
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Registrar Entrada",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(modifier = Modifier.padding(0.dp)) {
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
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            // Campo de Código del Producto
            OutlinedTextField(
                value = productCode,
                onValueChange = { productCode = it },
                label = { Text("Código del producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            // Mensaje si el producto no existe
            if (productCode.isNotEmpty() && busqueda == null) {
                Text(
                    text = "El producto con Código $productCode no existe",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red
                )
            }






            Spacer(modifier = Modifier.height(8.dp))



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
                    .clickable {
                        isDatePickerVisible = true
                    } // Esto hará que al hacer clic en el campo se muestre el DatePicker
                    .border(
                        1.dp,
                        Color.DarkGray,
                        RoundedCornerShape(4.dp)
                    ) // Puedes agregar borde si lo deseas
            ) {
                Text(
                    text = if (fechaRegistro.isEmpty()) "Seleccionar Fecha" else fechaRegistro,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = if (fechaRegistro.isEmpty()) Color.DarkGray else Color.Black
                    ),
                    modifier = Modifier.padding(18.dp)
                )
            }




            Spacer(modifier = Modifier.height(8.dp))

            // Campo para Cantidad (puedes usar este campo como cantidad para la salida)
            OutlinedTextField(
                value = cantidadProduct,
                onValueChange = { cantidadProduct = it },
                label = { Text("Cantidad a Agregar") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )


        }
        if (creado) {
            Text(
                text = "Se agregó el Movimiento",
                color = Color.Blue,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Ocultar el mensaje después de 2 segundos
            LaunchedEffect(Unit) {
                delay(2000) // 2000 milisegundos = 2 segundos
                creado = false
            }
        }


    }

    Box(modifier = Modifier .fillMaxWidth() ,
            contentAlignment = Alignment.Center) {
        ElevatedButton(
            onClick = {

                if (productCode.isNotEmpty() && fechaRegistro.isNotEmpty() && cantidadProduct.isNotEmpty()) {

                    var mcode = productCode.toInt()
                    var mcantidad = cantidadProduct.toInt()
                    var mtipo = if (checkedState.value) "Salida" else "Entrada"




                    if (busqueda != null) {

                        mviewModel.insertMovimiento(
                            Movimientos(
                                codigoProducto = mcode,
                                tipoMovimiento = mtipo,
                                fecha = fechaRegistro,
                                cantidad = mcantidad
                            )
                        )


                        nuevoStock =
                            if (checkedState.value) stock - mcantidad else stock + mcantidad
                        viewModel.updateProducto(
                            Productos(
                                codigoProducto = mcode,
                                descripcion = descripcion,
                                numeroExistencia = nuevoStock,
                                costo = costo
                            )
                        )
                        creado = true
                    }
                    productCode = ""
                    fechaRegistro = ""
                    cantidadProduct = ""



                }


            },
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 0.dp),// Espaciado entre botones
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF090909), // Color del fondo del botón (puedes usar cualquier color que desees)
                contentColor = Color.White // Color del texto en el botón
            )
        ) {
            Text("Registar ${if (checkedState.value) "Salida" else "Entrada"}", fontSize = 18.sp)

        }
    }









}





@Preview(showBackground = true)
@Composable
fun BodyPreview_Registrar() {

    val appContainer = AppContainer(LocalContext.current)
    val navController = rememberNavController()
    Registrar_inventario(navController , appContainer)

}

