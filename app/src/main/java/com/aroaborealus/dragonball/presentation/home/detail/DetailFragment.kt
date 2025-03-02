package com.aroaborealus.dragonball.presentation.home.detail

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aroaborealus.dragonball.R
import com.aroaborealus.dragonball.databinding.FragmentDetailBinding
import com.aroaborealus.dragonball.presentation.home.HomeViewModel
import com.aroaborealus.dragonball.model.Character
import com.aroaborealus.dragonball.presentation.home.OpcionesJuego
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var job: Job? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("listaPersonajes", 0)

        initObservers()
        return binding.root
    }

    private fun initViews(personaje: Character) {
        with(binding) {
            Glide
                .with(binding.root)
                .load(personaje.imagenUrl)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            tvNombre.text = personaje.nombre
            pbVida.progress = personaje.vidaActual
            bGolpear.setOnClickListener {
                viewModel.golpearPersonaje(personaje, sharedPreferences)
                pbVida.progress = personaje.vidaActual
                checkAlive(personaje)
            }
            bCurar.setOnClickListener {
                viewModel.curarPersonaje(personaje, sharedPreferences)
                pbVida.progress = personaje.vidaActual
            }
            bCount.setOnClickListener {
                Toast.makeText(requireContext(), "${personaje.vecesSeleccionado} veces pulsado", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.guardarEstadoPersonaje(sharedPreferences,personaje)
    }

    private fun checkAlive(personaje: Character) {
        Log.e("DetailFragment",personaje.vecesSeleccionado.toString())
        if (personaje.vidaActual <= 0) {
            requireActivity().runOnUiThread {
                (activity as? OpcionesJuego)?.irAlListado()
            }
        }
    }


    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is HomeViewModel.State.PersonajeSeleccionado -> {
                        initViews(state.personaje)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        viewModel.personajeDeseleccionado(sharedPreferences)
    }
}
