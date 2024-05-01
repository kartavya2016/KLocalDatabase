package com.ksofttech.localdatabase.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun addUser(userModel: UserModel)

    @Query("select * from tblUser")
    fun getAllUser(): LiveData<List<UserModel>>

    @Query("delete from tblUser where id=:id")
    fun deleteUser(id: Int)

    @Query("update tblUser set name=:name,gender=:gender,email=:email,age=:age,imagePath=:imagePath where id=:id")
    fun updateUser(name:String,gender:String,email:String,age:String,imagePath:String,id: Int)
}