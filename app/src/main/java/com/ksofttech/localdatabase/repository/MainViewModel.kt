package com.ksofttech.localdatabase.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksofttech.localdatabase.db.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun addUser(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(userModel)
        }
    }

    fun getAllUser(): LiveData<List<UserModel>> {
        return userRepository.getAllUser()
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(id)
        }
    }

     fun updateUser(name:String,gender:String,email:String,age:String,imagePath:String,id: Int) {
        viewModelScope.launch (Dispatchers.IO){
            userRepository.updateUser(name,gender,email, age, imagePath, id)
        }
    }
}