package com.example.edtech

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edtech.screens.ChatScreen
import com.example.edtech.screens.SignInScreen
import com.example.edtech.screens.StudentHomeScreen
import com.example.edtech.screens.TeacherChatsScreen
import com.example.edtech.screens.TeacherDashboard
import com.example.edtech.screens.TestScreen


enum class EdTechAppScreens(val title : String) {
    SignIn(title = "SignIn"),
    TeacherDashboard(title = "TeacherDashboard"),
    StudentHome(title = "StudentHome"),
    TeacherChats(title = "TeacherChats"),
    ChatScreen(title = "ChatScreen")
}

@Composable
fun EdTechApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route ?: EdTechAppScreens.SignIn.name
    val baseRoute = route.substringBefore("/") // gets "ChatScreen" from "ChatScreen/{userEmail}"
    val currentScreen = try {
        EdTechAppScreens.valueOf(baseRoute)
    } catch (e: IllegalArgumentException) {
        EdTechAppScreens.SignIn // fallback if route doesn't match any enum
    }


    NavHost(
        navController = navController,
        startDestination = EdTechAppScreens.SignIn.name,
        modifier = Modifier
    ) {
        composable(route = EdTechAppScreens.SignIn.name) {
            SignInScreen(
                onSignInStudentClicked = {
                    navController.navigate(EdTechAppScreens.StudentHome.name) {
                        popUpTo(EdTechAppScreens.SignIn.name) { inclusive = true }
                    }
                },
                onSignInTeacherClicked = {
                    navController.navigate(EdTechAppScreens.TeacherDashboard.name) {
                        popUpTo(EdTechAppScreens.SignIn.name) { inclusive = true }
                    }
                },
                navController = navController
            )
        }
        composable(route = EdTechAppScreens.TeacherDashboard.name){
            TeacherDashboard(navController = navController)
        }
        composable(route = EdTechAppScreens.StudentHome.name){
            StudentHomeScreen(navController)
        }
        composable(route = EdTechAppScreens.TeacherChats.name) {
            TeacherChatsScreen(
                navController = navController,
            )
        }
        composable("${EdTechAppScreens.ChatScreen.name}/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail")
            ChatScreen(navController, userEmail = userEmail)
        }

    }
}