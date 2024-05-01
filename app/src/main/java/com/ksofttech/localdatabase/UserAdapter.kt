package com.ksofttech.localdatabase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksofttech.localdatabase.db.UserModel

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    var userArray: List<UserModel> = ArrayList<UserModel>()
    lateinit var clickToDelete: ClickToDelete

    fun setUserData(userArray: List<UserModel>) {
        this.userArray = userArray
        notifyDataSetChanged()
    }

    fun deleteUserData(clickToDelete: ClickToDelete) {
        this.clickToDelete = clickToDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_userdata, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return userArray.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val userModel = userArray[position]
        holder.tvName.text = "Name:${userModel.name}"
        holder.tvEmail.text = "Email:${userModel.email}"
        Glide.with(holder.itemView).load(userModel.imagePath).into(holder.ivUser)

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("id", userModel.id)
            intent.putExtra("name",userModel.name.toString())
            intent.putExtra("gender", userModel.gender)
            intent.putExtra("email", userModel.email)
            intent.putExtra("age", userModel.age)
            intent.putExtra("imagePath", userModel.imagePath)
            context.startActivity(intent)

        }

        holder.btnDelete.setOnClickListener {
            clickToDelete.delete(userModel)
        }
    }


    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUser = view.findViewById<ImageView>(R.id.ivUser)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
    }

    interface ClickToDelete {
        fun delete(userModel: UserModel)
    }
}