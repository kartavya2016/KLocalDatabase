package com.ksofttech.localdatabase

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ksofttech.localdatabase.databinding.ActivityAddUserBinding
import com.ksofttech.localdatabase.db.UserDatabase
import com.ksofttech.localdatabase.db.UserModel
import com.ksofttech.localdatabase.repository.MainViewModel
import com.ksofttech.localdatabase.repository.MainViewModelFact
import com.ksofttech.localdatabase.repository.UserRepository

class AddUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddUserBinding

    lateinit var mainViewModel: MainViewModel

    var gender: String = ""
    var imagePath: String = ""

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)

        val userDao = UserDatabase.getDatabase(application).userDao()
        val userRepository = UserRepository(userDao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFact(userRepository))[MainViewModel::class.java]

        binding.btnSave.setOnClickListener {
            addUser()
        }

        binding.rgGender.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                if (binding.rbMale.isChecked) {
                    gender = "Male"
                    Toast.makeText(applicationContext, gender, Toast.LENGTH_SHORT).show()
                } else if (binding.rbFeMale.isChecked) {
                    gender = "Female"
                    Toast.makeText(applicationContext, gender, Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.ivImage.setOnClickListener {
            requestStoragePermissions()
        }
    }

    private fun requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            chooseGallery()
        }
    }

    private fun chooseGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imagePath = imageUri.toString()
            Glide.with(applicationContext).load(imagePath).into(binding.ivImage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseGallery()
            } else {
                Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser() {

        if (binding.edtName.text!!.isEmpty()) {
            Toast.makeText(application, "Enter Name", Toast.LENGTH_SHORT).show()
        } else if (binding.edtEmail.text!!.isEmpty()) {
            Toast.makeText(application, "Enter Email", Toast.LENGTH_SHORT).show()
        } else if (binding.edtAge.text!!.isEmpty()) {
            Toast.makeText(application, "Enter Age", Toast.LENGTH_SHORT).show()
        } else {
            var uname = binding.edtName.text.toString()
            var uAge = binding.edtAge.text.toString()
            var uEmail = binding.edtEmail.text.toString()
            var uImage = imagePath
            val userModel = UserModel(0, uname, gender, uEmail, uAge, uImage)
            mainViewModel.addUser(userModel)
            Toast.makeText(applicationContext, "User Added Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}