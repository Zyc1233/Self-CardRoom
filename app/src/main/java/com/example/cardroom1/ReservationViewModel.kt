package com.example.cardroom1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations

    private val dao: ReservationDao

    init {
        val db = ReservationDatabase.getDatabase(application)
        dao = db.reservationDao()
        fetchReservations()
    }

    private fun fetchReservations() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getAllReservations().collect { reservationsList ->
                _reservations.value = reservationsList
            }
        }
    }

    suspend fun insertReservation(reservation: Reservation): Long? {
        val overlappingCount = dao.countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        if (overlappingCount > 0) {
            return null
        }
        return dao.insertReservation(reservation)
    }

    suspend fun updateReservation(reservation: Reservation) {
        val overlappingCount = dao.countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        if (overlappingCount > 0) {
            return
        }
        dao.updateReservation(reservation)
    }

    suspend fun deleteReservation(reservationId: Long) {
        dao.deleteReservationById(reservationId)
        fetchReservations()
    }

    suspend fun isDuplicateReservation(reservation: Reservation): Boolean {
        val overlappingCount = dao.countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        return overlappingCount > 0
    }

    suspend fun getReservationById(reservationId: Long): Reservation? {
        return dao.getReservationById(reservationId)
    }
}