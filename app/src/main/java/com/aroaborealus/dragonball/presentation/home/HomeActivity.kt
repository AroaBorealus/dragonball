package com.aroaborealus.dragonball.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aroaborealus.dragonball.databinding.ActivityHomeBinding
import com.aroaborealus.dragonball.presentation.home.detail.DetailFragment
import com.aroaborealus.dragonball.presentation.home.list.ListFragment
import com.aroaborealus.dragonball.presentation.login.LoginViewModel
import kotlinx.coroutines.launch

interface OpcionesJuego{
    fun irAlListado()
    fun irAlDetalle()
}

class HomeActivity: AppCompatActivity(),OpcionesJuego {

    companion object{
        private val TAG_TOKEN = "Token"
        fun startJuegoActivity(context: Context, token: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(TAG_TOKEN, token)
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

        val token = intent.getStringExtra(TAG_TOKEN)
        token?.let{
            viewModel.actualizarToken(token)
        } ?: run {
            Toast.makeText(this, "No hay token. La activity se va a cerrar", Toast.LENGTH_LONG).show()
            finish()
        }
        //viewModel.getCharactersMock()
        initFragments()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state){
                    is HomeViewModel.State.Loading -> {
                        Log.i("HomeActivity", "State: Loading")
                    }
                    is HomeViewModel.State.Success -> {
                        Log.i("HomeActivity", state.characters[0].toString())
                    }
                    is HomeViewModel.State.Error -> {
                        Toast.makeText(this@HomeActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                    is HomeViewModel.State.SelectedCharacter -> {

                    }
                }

            }
        }
    }

    private fun initFragments() {
        irAlListado()
    }

    override fun irAlListado() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, ListFragment())
            addToBackStack(null)
            commit()
        }
    }
    override fun irAlDetalle() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, DetailFragment())
            addToBackStack(null)
            commit()
        }
    }

}