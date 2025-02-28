package com.aroaborealus.dragonball.presentation.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aroaborealus.dragonball.databinding.FragmentDetailBinding
import com.aroaborealus.dragonball.presentation.home.HomeViewModel
import com.aroaborealus.dragonball.model.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment: Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initObservers()
        return binding.root
    }

    private fun initViews(personaje: Character) {
        with(binding) {
            tvNombre.text = personaje.nombre
            pbVida.progress = personaje.vidaActual
            bGolpear.setOnClickListener {
                viewModel.golpearPersonaje(personaje) // TODO falta ue el golpe quite 20 puntos
                pbVida.progress = personaje.vidaActual
            }
            bCurar.setOnClickListener {
                personaje.vidaActual = personaje.vidaTotal // TODO falta curar 60 puntos
                pbVida.progress = personaje.vidaActual
            }
        }
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
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
        viewModel.personajeDeseleccionado()
    }
}