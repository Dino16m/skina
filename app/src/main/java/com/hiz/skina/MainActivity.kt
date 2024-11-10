package com.hiz.skina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.imePadding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hiz.skina.ui.screens.*
import com.hiz.skina.ui.theme.SkinaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkinaTheme {
                App()
            }
        }
    }
}

@Composable
fun App(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = LandingRoute) {

        composable<LandingRoute> {
            LandingPage(navigateToForm = {navController.navigate(route = NewRoutineRoute(action = RoutineAction.Create))})
        }
        composable<NewRoutineRoute> {
            val route = it.toRoute<NewRoutineRoute>()

            NewRoutinePage(navigateBack = {navController.navigate(route = LandingRoute)}, action = route.action)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    SkinaTheme {
        App()
    }
}