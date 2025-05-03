package com.example.firebase_sinin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.firebase_sinin.Screens.HomeScreen
import com.example.firebase_sinin.Screens.SignInScreen

enum class SigninAppScreens(val title : String) {
    SignIn(title = "SignIn"),
    Home(title = "Home")
}

@Composable
fun SignInApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SigninAppScreens.valueOf(
        backStackEntry?.destination?.route ?: SigninAppScreens.SignIn.name
    )

    NavHost(
        navController = navController,
        startDestination = SigninAppScreens.SignIn.name,
        modifier = Modifier
    ) {
        composable(route = SigninAppScreens.SignIn.name) {
            SignInScreen(
                onSignInClicked = {
                    navController.navigate(SigninAppScreens.Home.name) {
                        popUpTo(SigninAppScreens.SignIn.name){inclusive = true}
                    }
                },
            )
        }
        composable(route = SigninAppScreens.Home.name) {
            HomeScreen(
                onSignOutClicked = {
                    navController.navigate(SigninAppScreens.SignIn.name){
                        popUpTo (SigninAppScreens.Home.name) {inclusive = true}
                    }
                }
            )
        }
    }
}