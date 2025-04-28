package com.example.myapplication.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
    @Update
    suspend fun update(user: User)
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getUsers(): List<User>
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User?
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}