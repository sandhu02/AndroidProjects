package com.example.edtech.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edtech.EdTechViewModelFactory
import com.example.edtech.ui.theme.EdTechTheme
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.edtech.EdTechAppScreens

@Composable
fun SignInScreen(
    viewModel : SignInViewModel = viewModel (
        factory = EdTechViewModelFactory (LocalContext.current)
    ),
    onSignInStudentClicked:() -> Unit = {},
    onSignInTeacherClicked:() -> Unit = {},
    navController: NavController
) {
    val primaryColor = Color(0xFF4285F4)
    val secondaryColor = Color(0xFF34A853)

    val uiState by viewModel.signInScreenUiState.collectAsState()
    val signInstate by viewModel.signInUiState.collectAsState()
    val signUpstate by viewModel.signUpUiState.collectAsState()

    LaunchedEffect(signInstate.signInSuccess) {
        if (signInstate.signInSuccess && uiState.role == "student") {
            onSignInStudentClicked()
            viewModel.resetSignInSuccess()
        }
        else if (signInstate.signInSuccess && uiState.role == "teacher") {
            onSignInTeacherClicked()
            viewModel.resetSignInSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo or Icon
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "EdTech Logo",
                modifier = Modifier.size(72.dp),
                tint = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Title
            Text(
                text = "EdTech Platform",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (uiState.isSignIn) "Sign in to continue" else "Create a new account",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))


            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.switchRole("student") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.role == "student") secondaryColor else Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Student",
                            tint = if (uiState.role == "student") Color.White else Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Student",
                            color = if (uiState.role == "student") Color.White else Color.DarkGray
                        )
                    }
                }

                Button(
                    onClick = { viewModel.switchRole("teacher") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.role == "teacher") secondaryColor else Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupervisorAccount,
                            contentDescription = "Teacher",
                            tint = if (uiState.role == "teacher") Color.White else Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Teacher",
                            color = if (uiState.role == "teacher") Color.White else Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Email Field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    // Use alternative icons from the default Icons.Default package
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.showPasword) Icons.Default.Check else Icons.Default.Lock,
                            contentDescription = if (uiState.showPasword) "Hide Password" else "Show Password"
                        )
                    }
                },
                visualTransformation = if (uiState.showPasword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true
            )

            // Confirm Password Field (only for registration)
            if (!uiState.isSignIn) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.updateConfirmPassword(it) },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Enter your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.PersonOutline, contentDescription = "enter name")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
            }

            // Forgot Password (only for sign in)
            if (uiState.isSignIn) {
                TextButton(
                    onClick = { /* Handle forgot password */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password?", color = primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In/Register Button
            Button(
                onClick = {
                    if (uiState.isSignIn) {
                        viewModel.signIn()
                    } else {
                        Log.d("RegisterCheck", "role=${uiState.role}, email=${uiState.email}")
                        viewModel.register()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = uiState.role.isNotEmpty() && uiState.email.isNotEmpty() && uiState.password.isNotEmpty() &&
                        (uiState.isSignIn || ( uiState.isSignIn.equals(false) && uiState.confirmPassword.isNotEmpty()))
            ) {
                if (signInstate.isLoading || signUpstate.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
                else {
                    Text(
                        text = if (uiState.isSignIn) "Sign In" else "Register",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle between Sign In and Register
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.isSignIn) "Don't have an account? " else "Already have an account? ",
                    color = Color.Gray
                )

                TextButton(onClick = { viewModel.toggleSignInMode() }) {
                    Text(
                        text = if (uiState.isSignIn) "Register" else "Sign In",
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp) )
            if (signInstate.error!=null){Text(signInstate.error.toString(), color = Color.Red)}
            else if (signUpstate.error!=null){Text(signUpstate.error.toString(), color = Color.Red)}

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(EdTechAppScreens.TeacherDashboard.name) },
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text("Guest")
            }
        }
    }
}

