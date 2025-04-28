package com.example.myapplication.user


class UserRepository(private val api: UserService) {
    fun fetchFirstUserName(): String {
        return try {
            val users = api.getUsers()
            if (users.isEmpty()) {
                "No users found"
            } else {
                "First user: ${users[0].name}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}