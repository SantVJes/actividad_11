package com.example.actividad_11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.actividad_11.Database.AppContainer
import com.example.actividad_11.navegacion.AppNavigations
import com.example.actividad_11.ui.theme.Actividad_11Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad_11Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                  AppNavigations(appContainer  = AppContainer(this))
                }
            }
        }
    }
}




