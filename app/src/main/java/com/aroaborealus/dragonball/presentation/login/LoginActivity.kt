package com.aroaborealus.dragonball.presentation.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aroaborealus.dragonball.presentation.home.HomeActivity
import com.aroaborealus.dragonball.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()

        binding.bLogin.setOnClickListener {
            viewModel.iniciarLogin(
                usuario = binding.etUser.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state){
                    is LoginViewModel.State.Idle -> {}
                    is LoginViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is LoginViewModel.State.Success -> {
                        viewModel.guardarUsuario(
                            preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE),
                            usuario = binding.etUser.text.toString(),
                            password = binding.etPassword.text.toString()
                        )
                        binding.pbLoading.visibility = View.INVISIBLE
                        startJuegoActivity()
                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun startJuegoActivity() {
        HomeActivity.startJuegoActivity(this)
        finish()
    }
}