package com.example.cardroom1

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// 数据库抽象类
@Database(entities = [Reservation::class], version = 1)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "reservations.db"
                ).build().also { instance = it }
            }
        }
    }
}

// 预约管理单例对象
object ReservationManager {
    // 新增预订
    suspend fun insertReservation(context: Context, reservation: Reservation): Long? {
        val database = DatabaseHelper.getInstance(context)
        val overlappingCount = database.reservationDao().countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        if (overlappingCount > 0) {
            Log.e("ReservationManager", "该时间段已有预约，无法重复预约")
            return null
        }
        return database.reservationDao().insertReservation(reservation)
    }
    // 检查是否存在重复预约
    suspend fun isDuplicateReservation(context: Context, reservation: Reservation): Boolean {
        val database = DatabaseHelper.getInstance(context)
        val overlappingCount = database.reservationDao().countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        return overlappingCount > 0
    }

    // 查询所有预订
    private fun getAllReservations(context: Context): LiveData<List<Reservation>> {
        return DatabaseHelper.getInstance(context).reservationDao().getAllReservations()
    }

    // 删除预订
    suspend fun deleteReservationById(context: Context, reservationId: Long) {
        try {
            DatabaseHelper.getInstance(context).reservationDao().deleteReservationById(reservationId)
        } catch (e: Exception) {
            Log.e("ReservationManager", "删除失败: ${e.message}", e)
            throw e
        }
    }

    // 更新预订
    suspend fun updateReservation(context: Context, reservation: Reservation) {
        val database = DatabaseHelper.getInstance(context)
        val overlappingCount = database.reservationDao().countOverlappingReservations(
            reservation.room,
            reservation.date,
            reservation.time1,
            reservation.time2
        )
        if (overlappingCount > 0) {
            // 如果存在重叠预约，抛出异常或者返回错误信息
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "该时间段已有预约，无法修改", Toast.LENGTH_SHORT).show()
            }
            return
        }
        database.reservationDao().updateReservation(reservation)
    }
}