package com.icjardinapps.dm2.bakio.Ranking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icjardinapps.dm2.bakio.ConexionBBDD.ConexionDb
import com.icjardinapps.dm2.bakio.Portada.PortadaDeLaApp
import com.icjardinapps.dm2.bakio.R

class Ranking : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        recyclerView = findViewById(R.id.recyclerViewRanking)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Iniciar el adaptador vacío (se actualizará después)
        rankingAdapter = RankingAdapter(emptyList())
        recyclerView.adapter = rankingAdapter

        // Obtener los datos de la base de datos en un hilo secundario
        Thread {
            val bd = ConexionDb(this)
            val rankingData = bd.ranking()

            // Actualizar la UI en el hilo principal
            runOnUiThread {
                if (rankingData.isNotEmpty()) {
                    rankingAdapter = RankingAdapter(rankingData) // Crear el nuevo adaptador con los datos
                    recyclerView.adapter = rankingAdapter // Asignar el adaptador actualizado
                } else {
                    Toast.makeText(this, "No hay datos de ranking disponibles.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()

        // Configurar el botón de "volver" para regresar a la portada
        val btnBackToWelcome: ImageButton = findViewById(R.id.imageButtonBackToWelcome)
        btnBackToWelcome.setOnClickListener {
            // Iniciar la actividad de la portada
            val intent = Intent(this, PortadaDeLaApp::class.java)
            startActivity(intent)
            finish() // Finalizar la actividad actual para evitar que se quede en la pila de actividades
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
