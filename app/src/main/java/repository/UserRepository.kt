package repository

import dao.UserDao
import table.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(email: String, password: String, name: String, levelId: Int): Long {
        val user = User(email = email, passwordHash = password, name = name, levelId = levelId)
        return userDao.insertUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.passwordHash == password) user else null
    }

    suspend fun getUser(userId: Long): User? = userDao.getUserById(userId)
}