package com.ksofttech.localdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ksofttech.localdatabase.databinding.ActivityUpdateBinding
import com.ksofttech.localdatabase.db.UserDatabase
import com.ksofttech.localdatabase.repository.MainViewModel
import com.ksofttech.localdatabase.repository.MainViewModelFact
import com.ksofttech.localdatabase.repository.UserRepository

class UpdateActivity : AppCompatActivity() {

    lateinit var updateBinding: ActivityUpdateBinding

    lateinit var mainViewModel: MainViewModel

    var imagePath: String = ""

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    var name: String? = null
    var age: String? = null
    var gender: String? = null
    var email: String? = null
    var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = DataBindingUtil.setContentView(this, R.layout.activity_update)
        val userDao = UserDatabase.getDatabase(application).userDao()
        val userRepository = UserRepository(userDao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFact(userRepository))[MainViewModel::class.java]

        getDatafromIntent()
        setData()
        clickListenr()
    }


    private fun getDatafromIntent() {
        id = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name")
        age = intent.getStringExtra("age")
        email = intent.getStringExtra("email")
        gender = intent.getStringExtra("gender")
        imagePath = intent.getStringExtra("imagePath").toString()
    }


    private fun setData() {
        updateBinding.edtName.setText(name)
        updateBinding.edtAge.setText(age)
        updateBinding.edtEmail.setText(email)
        Glide.with(applicationContext).load(imagePath).into(updateBinding.ivImage)

        if (gender.equals("Male")) {
            updateBinding.rbMale.isChecked = true
            gender = "Male"
        }

        if (gender.equals("Female")) {
            updateBinding.rbFeMale.isChecked = true
            gender = "Female"
        }
    }

    private fun clickListenr() {

        updateBinding.btnSave.setOnClickListener {

            var uname = updateBinding.edtName.text.toString()
            var uage = updateBinding.edtAge.text.toString()
            var uemail = updateBinding.edtEmail.text.toString()
            var uimage = imagePath
            var ugender = gender

            mainViewModel.updateUser(uname, ugender!!, uemail, uage, uimage, id!!)
            Toast.makeText(applicationContext, "User Updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
}