package com.ksofttech.localdatabase

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksofttech.localdatabase.databinding.ActivityMainBinding
import com.ksofttech.localdatabase.db.UserDatabase
import com.ksofttech.localdatabase.db.UserModel
import com.ksofttech.localdatabase.repository.MainViewModel
import com.ksofttech.localdatabase.repository.MainViewModelFact
import com.ksofttech.localdatabase.repository.UserRepository

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var mainViewModel: MainViewModel
    lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val userDao = UserDatabase.getDatabase(application).userDao()
        val userRepository = UserRepository(userDao)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFact(userRepository))[MainViewModel::class.java]

        userAdapter = UserAdapter(applicationContext)

        binding.btnAddNew.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        binding.btnAgeGraph.setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
        }

        obserUserData()
        clicktoDelete()
    }

    private fun clicktoDelete() {
        userAdapter.deleteUserData(object : UserAdapter.ClickToDelete {
            override fun delete(userModel: UserModel) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Remove!!")
                alertDialog.setMessage("Are you sure you want to Remove? ${userModel.name}")
                alertDialog.setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        mainViewModel.deleteUser(userModel.id)
                        Toast.makeText(applicationContext, "User Removed", Toast.LENGTH_SHORT)
                            .show()
                        dialogInterface.dismiss()
                    })
                alertDialog.setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                alertDialog.create()
                alertDialog.show()
            }
        })
    }

    private fun obserUserData() {
        mainViewModel.getAllUser().observe(this) {
            binding.rvUser.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                userAdapter.setUserData(it)
                itemAnimator = DefaultItemAnimator()
                adapter = userAdapter
            }
        }
    }
}