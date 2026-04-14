package com.example.uber

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.uber.model.RideHistoryItem
import com.example.uber.model.RideType
import com.example.uber.navigation.Screen
import com.example.uber.ui.BookingConfirmationScreen
import com.example.uber.ui.BottomNavBar
import com.example.uber.ui.DriverAssignedScreen
import com.example.uber.ui.HistoryScreen
import com.example.uber.ui.HomeScreen
import com.example.uber.ui.LoginScreen
import com.example.uber.ui.PaymentScreen
import com.example.uber.ui.ProfileScreen
import com.example.uber.ui.RegisterScreen
import com.example.uber.ui.RideCompleteScreen
import com.example.uber.ui.RideSelectionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UberCloneApp() {
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

    var userName by remember { mutableStateOf("") }
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }

    var pickupLocation by remember { mutableStateOf("") }
    var dropLocation by remember { mutableStateOf("") }

    val rideTypes = listOf(
        RideType("Bike", 90, "2 min"),
        RideType("Mini", 150, "4 min"),
        RideType("Sedan", 230, "6 min"),
        RideType("SUV", 320, "8 min")
    )

    var selectedRide by remember { mutableStateOf<RideType?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Cash") }
    var driverName by remember { mutableStateOf("Rohan Patil") }
    var vehicleNumber by remember { mutableStateOf("MH 01 AB 2026") }
    var driverPhone by remember { mutableStateOf("+91 9876543210") }

    val rideHistory = remember { mutableStateListOf<RideHistoryItem>() }

    when (currentScreen) {
        Screen.LOGIN -> {
            LoginScreen(
                email = loginEmail,
                password = loginPassword,
                onEmailChange = { loginEmail = it },
                onPasswordChange = { loginPassword = it },
                onLoginClick = {
                    if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                        userName = "Sahil"
                        currentScreen = Screen.HOME
                    }
                },
                onRegisterClick = {
                    currentScreen = Screen.REGISTER
                }
            )
        }

        Screen.REGISTER -> {
            RegisterScreen(
                name = registerName,
                email = registerEmail,
                password = registerPassword,
                onNameChange = { registerName = it },
                onEmailChange = { registerEmail = it },
                onPasswordChange = { registerPassword = it },
                onRegisterClick = {
                    if (
                        registerName.isNotEmpty() &&
                        registerEmail.isNotEmpty() &&
                        registerPassword.isNotEmpty()
                    ) {
                        userName = registerName
                        currentScreen = Screen.HOME
                    }
                },
                onBackToLogin = {
                    currentScreen = Screen.LOGIN
                }
            )
        }

        Screen.HOME, Screen.HISTORY, Screen.PROFILE -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = when (currentScreen) {
                                    Screen.HOME -> "QuickRide"
                                    Screen.HISTORY -> "Ride History"
                                    Screen.PROFILE -> "Profile"
                                    else -> ""
                                }
                            )
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        currentScreen = currentScreen,
                        onHomeClick = { currentScreen = Screen.HOME },
                        onHistoryClick = { currentScreen = Screen.HISTORY },
                        onProfileClick = { currentScreen = Screen.PROFILE }
                    )
                }
            ) { paddingValues ->
                when (currentScreen) {
                    Screen.HOME -> {
                        HomeScreen(
                            modifier = Modifier.padding(paddingValues),
                            userName = userName,
                            pickupLocation = pickupLocation,
                            dropLocation = dropLocation,
                            onPickupChange = { pickupLocation = it },
                            onDropChange = { dropLocation = it },
                            onFindRideClick = {
                                if (pickupLocation.isNotEmpty() && dropLocation.isNotEmpty()) {
                                    currentScreen = Screen.RIDE_SELECTION
                                }
                            }
                        )
                    }

                    Screen.HISTORY -> {
                        HistoryScreen(
                            modifier = Modifier.padding(paddingValues),
                            historyList = rideHistory
                        )
                    }

                    Screen.PROFILE -> {
                        ProfileScreen(
                            modifier = Modifier.padding(paddingValues),
                            userName = if (userName.isNotEmpty()) userName else "User",
                            userEmail = if (loginEmail.isNotEmpty()) loginEmail else registerEmail,
                            onLogoutClick = {
                                loginEmail = ""
                                loginPassword = ""
                                registerName = ""
                                registerEmail = ""
                                registerPassword = ""
                                pickupLocation = ""
                                dropLocation = ""
                                selectedRide = null
                                selectedPaymentMethod = "Cash"
                                currentScreen = Screen.LOGIN
                            }
                        )
                    }

                    else -> Unit
                }
            }
        }

        Screen.RIDE_SELECTION -> {
            RideSelectionScreen(
                pickup = pickupLocation,
                drop = dropLocation,
                rideTypes = rideTypes,
                selectedRide = selectedRide,
                onRideSelected = { selectedRide = it },
                onBackClick = { currentScreen = Screen.HOME },
                onContinueClick = {
                    if (selectedRide != null) {
                        currentScreen = Screen.BOOKING_CONFIRMATION
                    }
                }
            )
        }

        Screen.BOOKING_CONFIRMATION -> {
            BookingConfirmationScreen(
                pickup = pickupLocation,
                drop = dropLocation,
                rideType = selectedRide,
                onBackClick = { currentScreen = Screen.RIDE_SELECTION },
                onConfirmClick = { currentScreen = Screen.DRIVER_ASSIGNED }
            )
        }

        Screen.DRIVER_ASSIGNED -> {
            DriverAssignedScreen(
                driverName = driverName,
                vehicleNumber = vehicleNumber,
                driverPhone = driverPhone,
                rideType = selectedRide,
                onBackClick = { currentScreen = Screen.BOOKING_CONFIRMATION },
                onStartRideClick = { currentScreen = Screen.PAYMENT }
            )
        }

        Screen.PAYMENT -> {
            PaymentScreen(
                rideType = selectedRide,
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentSelected = { selectedPaymentMethod = it },
                onBackClick = { currentScreen = Screen.DRIVER_ASSIGNED },
                onPayClick = {
                    selectedRide?.let { ride ->
                        rideHistory.add(
                            RideHistoryItem(
                                from = pickupLocation,
                                to = dropLocation,
                                rideType = ride.name,
                                fare = ride.price,
                                paymentMethod = selectedPaymentMethod
                            )
                        )
                    }
                    currentScreen = Screen.RIDE_COMPLETE
                }
            )
        }

        Screen.RIDE_COMPLETE -> {
            RideCompleteScreen(
                rideType = selectedRide,
                paymentMethod = selectedPaymentMethod,
                onGoHomeClick = {
                    pickupLocation = ""
                    dropLocation = ""
                    selectedRide = null
                    selectedPaymentMethod = "Cash"
                    currentScreen = Screen.HOME
                }
            )
        }
    }
}