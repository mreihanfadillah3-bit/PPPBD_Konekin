package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityMainBinding
import com.example.konekin.model.Data
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dataList: List<Data> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvNama.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("USER_ID", dataList[position].id)
            startActivity(intent)
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, UserFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun loadUsers() {
        ApiClient.getInstance().getAllUsers().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    dataList = response.body() ?: emptyList()
                    val names = dataList.map { it.employeeName }
                    binding.lvNama.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        names
                    )
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal memuat data (${response.code()})",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }
}