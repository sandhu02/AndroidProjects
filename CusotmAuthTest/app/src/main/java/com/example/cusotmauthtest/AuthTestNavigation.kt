package com.example.cusotmauthtest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cusotmauthtest.screens.HomeScreen
import com.example.cusotmauthtest.screens.RegisterScreen
import com.example.cusotmauthtest.screens.SignInScreen

enum class AuthTestScreens(val title: String){
    SignInScreen(title = "SignIn"),
    RegisterScreen(title = "Register"),
    HomeScreen(title = "Home")
}

@Composable
fun AuthTestApp(
    navController : NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController ,
        startDestination = AuthTestScreens.SignInScreen.name ,
        modifier = Modifier
    ){
        composable(route = AuthTestScreens.RegisterScreen.name) {
            RegisterScreen(navController = navController)
        }
        composable(route = AuthTestScreens.SignInScreen.name) {
            SignInScreen(navController = navController)
        }
        composable(route = AuthTestScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
    }
}