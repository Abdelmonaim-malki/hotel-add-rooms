package com.example.api_3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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

data class AddResponse(
    val status: Int,
    val status_message: String
)

class AddActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextNumero = findViewById<EditText>(R.id.numero)
        val editTextType = findViewById<EditText>(R.id.type)
        val editTextNuite = findViewById<EditText>(R.id.nuite)
        val checkBoxDisponible = findViewById<CheckBox>(R.id.dis)
        val buttonAddRoom = findViewById<Button>(R.id.btn_add_room)

        // Initialisation de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiyes.net/") // Remplacez par votre URL API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        buttonAddRoom.setOnClickListener {
            val numero = editTextNumero.text.toString().trim()
            val type = editTextType.text.toString().trim()
            val nuite = editTextNuite.text.toString().trim()
            val max_personnes = editTextNuite.text.toString().trim()
            val url_image = editTextNuite.text.toString().trim()
            val disponible = if (checkBoxDisponible.isChecked) "Yes" else "No"

            if (numero.isEmpty() || type.isEmpty() || nuite.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val hotel = Hotel(
                    numero = numero.toInt(),
                    type = type,
                    nuite = nuite.toInt(),
                    disponible = disponible,
                    max_personnes = max_personnes.toInt(), // Set default value or get it from input if needed
                    url_image = url_image // Set default or get it from input
                )

                // Appel à l'API pour ajouter la chambre
                apiService.addRoom(hotel).enqueue(object : Callback<AddResponse> {
                    override fun onResponse(
                        call: Call<AddResponse>,
                        response: Response<AddResponse>
                    ) {
                        if (response.isSuccessful) {
                            val addResponse = response.body()
                            if (addResponse != null) {
                                Toast.makeText(
                                    applicationContext,
                                    addResponse.status_message,
                                    Toast.LENGTH_LONG
                                ).show()
                                if (addResponse.status == 1) {
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Failed to add room",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Retro Error", t.message.toString())
                    }
                })
            }
        }
    }
}
