package com.example.cardroom1

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user") val user: String,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time1") val time1: String,
    @ColumnInfo(name = "time2") val time2: String
)

@Dao
interface ReservationDao {

    @Insert
    suspend fun insertReservation(reservation: Reservation): Long

    @Query("SELECT * FROM reservations")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Query("DELETE FROM reservations WHERE id = :reservationId")
    suspend fun deleteReservationById(reservationId: Long)

    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    suspend fun getReservationById(reservationId: Long): Reservation?

    @Update
    suspend fun updateReservation(reservation: Reservation)

    @Query("SELECT COUNT(*) FROM reservations " +
            "WHERE room = :room AND date = :date " +
            "AND ( (time1 <= :startTime AND time2 >= :startTime) " +
            " OR (time1 <= :endTime AND time2 >= :endTime) " +
            " OR (time1 >= :startTime AND time2 <= :endTime) )")
    suspend fun countOverlappingReservations(
        room: String,
        date: String,
        startTime: String,
        endTime: String
    ): Int
}



