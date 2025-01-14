package com.example.cardroom1

import androidx.room.*


// 用户实体类
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 0,
    val phone: String,
    val password: String,
    val code: String = ""
)

// 数据库访问接口
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password")
    suspend fun getUser(phone: String, password: String): User?

    @Query("SELECT * FROM users WHERE phone = :phone AND code = :code")
    suspend fun getUserByPhoneAndCode(phone: String, code: String): User?

    @Query("SELECT * FROM users WHERE phone = :phone")
    suspend fun getUserByPhone(phone: String): User?

    @Transaction
    suspend fun resetUserPassword(phone: String, newPassword: String) {
        val user = getUserByPhone(phone)
        if (user!= null) {
            val updatedUser = user.copy(password = newPassword)
            insertUser(updatedUser)
        }
    }
}

