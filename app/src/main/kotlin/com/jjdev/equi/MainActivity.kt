package com.jjdev.equi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.jjdev.equi.core.ui.theme.EquiTheme
import com.jjdev.equi.dashboard.presentation.DashboardScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EquiTheme {
                Scaffold { contentPadding ->
                    DashboardScreen(modifier = Modifier.padding(contentPadding))
                }
            }
        }
    }
}