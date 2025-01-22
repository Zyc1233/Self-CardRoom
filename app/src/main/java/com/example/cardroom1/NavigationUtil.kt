package com.example.cardroom1


import java.text.SimpleDateFormat
import java.util.Locale


object NavigationUtil {
    fun validateReservation(reservation: Reservation): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
        return try {
            val startTime = sdf.parse(reservation.time1)
            val endTime = sdf.parse(reservation.time2)
            startTime!= null && endTime!= null &&!startTime.after(endTime)
        } catch (e: Exception) {
            false
        }
    }
}