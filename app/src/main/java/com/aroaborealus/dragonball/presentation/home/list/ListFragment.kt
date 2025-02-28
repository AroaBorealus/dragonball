package com.aroaborealus.dragonball.presentation.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aroaborealus.dragonball.databinding.FragmentListBinding
import com.aroaborealus.dragonball.presentation.home.HomeViewModel
import kotlinx.coroutines.launch
import androidx.fragment.app.activityViewModels
import com.aroaborealus.dragonball.presentation.home.OpcionesJuego
import kotlinx.coroutines.Job


class ListFragment: Fragment() {

    private val personajesAdapter = CharacterAdapter(
        onPersonajeClicked = { personaje ->
            viewModel.personajeSeleccionado(personaje)
        }
    )
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentListBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() {
        binding.rvPersonajes.layoutManager = LinearLayoutManager(this.context)
        binding.rvPersonajes.adapter = personajesAdapter
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is HomeViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is HomeViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        personajesAdapter.actualizarPersonajes(state.personajes)
                    }
                    is HomeViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.GONE
                    }
                    is HomeViewModel.State.PersonajeSeleccionado -> {
                        (activity as? OpcionesJuego)?.irAlDetalle()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

}