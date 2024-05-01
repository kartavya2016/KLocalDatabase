package com.ksofttech.localdatabase.repository

import androidx.lifecycle.LiveData
import com.ksofttech.localdatabase.db.UserDao
import com.ksofttech.localdatabase.db.UserModel

class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(userModel: UserModel) {
        userDao.addUser(userModel)
    }

    fun getAllUser(): LiveData<List<UserModel>> {
        return userDao.getAllUser()
    }

    fun deleteUser(id: Int) {
        return userDao.deleteUser(id)
    }

    fun updateUser(name:String,gender:String,email:String,age:String,imagePath:String,id: Int)
    {
        return userDao.updateUser(name,gender,email, age, imagePath, id)
    }

}