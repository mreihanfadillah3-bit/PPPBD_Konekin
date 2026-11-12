package com.example.konekin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konekin.databinding.ActivityUserFormBinding
import com.example.konekin.model.Data
import com.example.konekin.model.UserRequest
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFormBinding
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID")

        if (userId != null) {
            title = "Edit User"
            binding.etName.setText(intent.getStringExtra("USER_NAME"))
            binding.etAge.setText(intent.getIntExtra("USER_AGE", 0).toString())
            binding.etSalary.setText(intent.getIntExtra("USER_SALARY", 0).toString())
        } else {
            title = "Tambah User"
        }

        binding.btnSave.setOnClickListener { saveUser() }
    }

    private fun saveUser() {
        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().toIntOrNull()
        val salary = binding.etSalary.text.toString().toIntOrNull()

        if (name.isEmpty() || age == null || salary == null) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val body = UserRequest(name, salary, age)
        val api = ApiClient.getInstance()
        val id = userId

        val call = if (id == null) api.addUser(body) else api.updateUser(id, body)

        call.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val msg = if (id == null) "User ditambahkan" else "User diperbarui"
                    Toast.makeText(this@UserFormActivity, msg, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@UserFormActivity,
                        "Gagal menyimpan (${response.code()})",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@UserFormActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
}