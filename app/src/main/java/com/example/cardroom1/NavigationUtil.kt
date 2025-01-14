package com.example.cardroom1


import java.text.SimpleDateFormat
import java.util.Locale


object NavigationUtil {
//    fun putReservationData(
//        navController: NavController,
//        reservation: Reservation
//    ) {
//        val bundle = Bundle().apply {
//            putLong("reservationId", reservation.id)
//            putString("userName", reservation.user)
//            putString("selectedRoomsText", reservation.room)
//            putString("selectedDate", reservation.date)
//            putString("selectedStartTime", reservation.time1)
//            putString("selectedEndTime", reservation.time2)
//        }
//        Log.d("NavigationUtil", "Put data: $bundle")
//        navController.previousBackStackEntry?.savedStateHandle?.set("reservationData", bundle)
//    }

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