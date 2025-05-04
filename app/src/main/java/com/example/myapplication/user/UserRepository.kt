package com.example.myapplication.user


class UserRepository(private val api: UserService) {
    suspend fun fetchFirstUserName(): String {
        return try {
            val users = api.getUsers()
            if (users.isNotEmpty()) {
                "First user: ${users[0].name}"
            } else {
                "No users found"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}