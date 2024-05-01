package com.ksofttech.localdatabase.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblUser")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val gender: String,
    val email: String,
    val age: String,
    val imagePath: String
)
