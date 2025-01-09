package com.example.cardroom1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 数据库抽象类
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "app_database"
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build().also { instance = it }
        }
    }
}

// 数据库提供器
object DatabaseProvider {
    fun getDatabase(context: Context): UserDao {
        try {
            return AppDatabase.getInstance(context).userDao()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}