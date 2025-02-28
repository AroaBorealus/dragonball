package com.aroaborealus.dragonball.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aroaborealus.dragonball.databinding.ActivityHomeBinding
import com.aroaborealus.dragonball.presentation.home.detail.DetailFragment
import com.aroaborealus.dragonball.presentation.home.list.ListFragment
import kotlin.random.Random

interface OpcionesJuego{
    fun irAlListado()
    fun irAlDetalle()
}

class HomeActivity: AppCompatActivity(),OpcionesJuego {

    companion object{
        fun startJuegoActivity(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.descargarPersonajes(getSharedPreferences("my_preferences", Context.MODE_PRIVATE))
        initFragments()
    }

    private fun initFragments() {
        irAlListado()
    }

    override fun irAlListado() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, ListFragment())
            commit()
        }
    }
    override fun irAlDetalle() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, DetailFragment())
            addToBackStack(Random.nextInt().toString())
            commit()
        }
    }

}