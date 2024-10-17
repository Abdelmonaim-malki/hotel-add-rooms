package com.example.api_3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Hotel(
    val numero:Int,
    val type:String,
    val nuite:Int,
    val disponible:String,
    val max_personnes: Int,
    val url_image:String
)
class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainn)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addButt = findViewById<Button>(R.id.add)

        addButt.setOnClickListener {

            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)

        }



        val listView = findViewById<ListView>(R.id.lv)

        val retrofit= Retrofit.Builder()
            .baseUrl("https://apiyes.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService=retrofit.create(ApiService::class.java)

        val call=apiService.getHotel()
        call.enqueue(object : Callback<List<Hotel>>{
            override fun onResponse(call: Call<List<Hotel>>, response: Response<List<Hotel>>) {
                if (response.isSuccessful) {
                    val cham = response.body() ?: emptyList()

                    val chambres = mutableListOf<String>()
                    for (s in cham) {
                        chambres.add(""+s.numero+" - "+s.nuite+ " MAD")
                    }

                    val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, chambres)
                    listView.adapter = adapter
                } else {
                    Toast.makeText(this@MainActivity, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Hotel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Échec de la connexion à l'API", Toast.LENGTH_SHORT).show()
            }
        })

    }
}