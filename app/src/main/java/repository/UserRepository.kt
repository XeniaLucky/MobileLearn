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

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserData(userId: Long, name: String, email: String, avatarPath: String?) {
        val user = userDao.getUserById(userId) ?: return
        val updatedUser = user.copy(name = name, email = email, avatarPath = avatarPath)
        userDao.updateUser(updatedUser)
    }

    suspend fun updateAvatar(userId: Long, avatarPath: String?) {
        val user = userDao.getUserById(userId) ?: return
        val updatedUser = user.copy(avatarPath = avatarPath)
        userDao.updateUser(updatedUser)
    }
}