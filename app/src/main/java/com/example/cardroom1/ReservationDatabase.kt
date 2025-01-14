package com.example.cardroom1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reservation::class], version = 1)
abstract class ReservationDatabase : RoomDatabase() {
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        var instance: ReservationDatabase? = null

        fun getInstance(context: Context): ReservationDatabase {
            return instance?: synchronized(this) {
                instance?: Room.databaseBuilder(
                    context.applicationContext,
                    ReservationDatabase::class.java,
                    "reservations.db"
                ).build().also { instance = it }
            }
        }

        fun getDatabase(context: Context): ReservationDatabase {
            return getInstance(context)
        }
    }
}