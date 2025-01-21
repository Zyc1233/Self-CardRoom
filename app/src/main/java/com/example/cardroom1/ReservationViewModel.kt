package com.example.cardroom1

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations

    private val dao: ReservationDao

    // LiveData 用于存储当前预约信息
    private val _currentReservation = MutableLiveData<Reservation?>(null)
    val currentReservation: LiveData<Reservation?> = _currentReservation

    fun setCurrentReservation(reservation: Reservation?) {
        _currentReservation.value = reservation
    }

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

    //插入预约信息
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
        val reservationId = dao.insertReservation(reservation)
        val insertedReservation = dao.getReservationById(reservationId)
        insertedReservation?.let {
            Log.d("InsertionLog", "ID: ${it.id}, Room: ${it.room}, Date: ${it.date}, Time: ${it.time1}-${it.time2}")
        }
        return reservationId
    }
    //更新预约信息
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
    //根据ID删除预约信息
    suspend fun deleteReservation(reservationId: Long) {
        dao.deleteReservationById(reservationId)
        fetchReservations()
    }
    //查询预约记录，防止重复预约
    suspend fun isDuplicateReservation(reservation: Reservation): Boolean {
        val overlappingCount = dao.countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        return overlappingCount > 0
    }
    //获取预约ID
    suspend fun getReservationById(reservationId: Long): Reservation? {
        return dao.getReservationById(reservationId)
    }
    //搜索预订人
    fun searchReservationsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchResults = dao.searchReservationsByName(name)
            _reservations.value = searchResults
        }
    }
}