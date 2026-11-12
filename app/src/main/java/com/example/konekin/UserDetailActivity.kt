package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konekin.databinding.ActivityUserDetailBinding
import com.example.konekin.model.Data
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private var userId: String = ""
    private var currentUser: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(this, "ID tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnEdit.setOnClickListener {
            val user = currentUser ?: return@setOnClickListener
            val intent = Intent(this, UserFormActivity::class.java)
            intent.putExtra("USER_ID", user.id)
            intent.putExtra("USER_NAME", user.employeeName)
            intent.putExtra("USER_AGE", user.employeeAge)
            intent.putExtra("USER_SALARY", user.employeeSalary)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener { confirmDelete() }
    }

    override fun onResume() {
        super.onResume()
        if (userId.isNotBlank()) loadDetail()
    }

    private fun loadDetail() {
        ApiClient.getInstance().getUserById(userId).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                val user = response.body()
                if (response.isSuccessful && user != null) {
                    currentUser = user
                    binding.tvId.text = "ID: ${user.id}"
                    binding.tvName.text = "Nama: ${user.employeeName}"
                    binding.tvAge.text = "Umur: ${user.employeeAge}"
                    binding.tvSalary.text = "Gaji: ${user.employeeSalary}"
                } else {
                    Toast.makeText(
                        this@UserDetailActivity,
                        "Gagal memuat data (${response.code()})",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Hapus User")
            .setMessage("Yakin ingin menghapus user ini?")
            .setPositiveButton("Hapus") { _, _ -> deleteUser() }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteUser() {
        ApiClient.getInstance().deleteUser(userId).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UserDetailActivity, "User dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@UserDetailActivity,
                        "Gagal menghapus (${response.code()})",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
}