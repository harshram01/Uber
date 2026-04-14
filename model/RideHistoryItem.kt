package com.example.uber.model

data class RideHistoryItem(
    val from: String,
    val to: String,
    val rideType: String,
    val fare: Int,
    val paymentMethod: String
)