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
import kotlinx.coroutines.launch
import kotlin.random.Random

class DetailFragment: Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is HomeViewModel.State.SelectedCharacter -> {
                        binding.tvNombre.text = state.character.nombre
                        binding.pbVida.progress = Random.nextInt(0,100)
                    }
                    else -> Unit
                }
            }
        }
    }
}