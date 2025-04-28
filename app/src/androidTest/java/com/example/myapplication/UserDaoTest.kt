package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.user.AppDatabase
import com.example.myapplication.user.User
import com.example.myapplication.user.UserDao
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class UserDaoTest {
    companion object {
        private lateinit var db: AppDatabase
        private lateinit var userDao: UserDao

        @JvmStatic
        @BeforeClass
        fun createDB() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            userDao = db.userDao()
        }

        @JvmStatic
        @AfterClass
        fun closeDB() {
            db.close()
        }
    }

    @Before
    fun clear() = runBlocking {
        userDao.deleteAll()
    }

    @Test
    fun insert() = runBlocking {
        val user = User(name = "Allen", email = "allen@gmail.com")
        userDao.insert(user)
        val users = userDao.getUsers()
        Assert.assertEquals(1, users.size)
        val firstItem = users[0]
        Assert.assertEquals(user.name, firstItem.name)
        Assert.assertEquals(user.email, firstItem.email)
        val retrievedUser = userDao.getUserById(firstItem.id)
        Assert.assertNotNull(retrievedUser)
        Assert.assertEquals(user.name, retrievedUser?.name)
        Assert.assertEquals(user.email, retrievedUser?.email)
    }

    @Test
    fun insertUsers() = runBlocking {
        val user1 = User(name = "Allen", email = "allen@gmail.com")
        val user2 = User(name = "Bob", email = "bob@gmail.com")
        userDao.insertUsers(listOf(user1, user2))
        val users = userDao.getUsers()
        Assert.assertEquals(2, users.size)
        Assert.assertEquals("Allen", users[0].name)
        Assert.assertEquals("Bob", users[1].name)
    }

    @Test
    fun update() = runBlocking {
        val user1 = User(name = "Allen", email = "allen@gmail.com")
        userDao.insert(user1)
        val insertUser = userDao.getUsers().first()
        val updateUser = User(id = insertUser.id, name = "David", email = "david@gmail.com")
        userDao.update(updateUser)

       val retrievedUser = userDao.getUserById(insertUser.id)
        Assert.assertNotNull(retrievedUser)
        Assert.assertEquals("David", retrievedUser?.name)
        Assert.assertEquals("david@gmail.com", retrievedUser?.email)
    }


}