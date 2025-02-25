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


class ListFragment: Fragment() {

    private val characterAdapter = CharacterAdapter(
        onCharacterClicked = { character ->
            viewModel.selectedCharacter(character)
        }
    )
    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentListBinding

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
        binding.rvCharacters.layoutManager = LinearLayoutManager(this.context)
        binding.rvCharacters.adapter = characterAdapter
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is HomeViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is HomeViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        characterAdapter.updateCharacters(state.characters)
                    }
                    is HomeViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.GONE
                    }
                    is HomeViewModel.State.SelectedCharacter -> {
                        (activity as? OpcionesJuego)?.irAlDetalle()
                    }
                }
            }
        }
    }

}