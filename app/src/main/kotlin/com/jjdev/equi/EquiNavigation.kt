package com.jjdev.equi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jjdev.equi.dashboard.presentation.DashboardScreen
import com.jjdev.equi.dashboard.presentation.DashboardViewModel

@Composable
fun EquiNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel.data.collectAsStateWithLifecycle().value,
                modifier,
            )
        }
    }
}