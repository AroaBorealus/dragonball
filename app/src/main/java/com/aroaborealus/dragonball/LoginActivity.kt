package com.aroaborealus.dragonball

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.aroaborealus.dragonball.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding ///by viewBinding(ActivityLoginBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        viewModel.guardarUsuario(
            preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE),
            usuario = "pepe",
            password = "1234"
        )
        Toast.makeText(this, "App abierta correctamente", Toast.LENGTH_LONG).show()
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
                        // TODO ir a la siguiente pantalla
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "El token es. ${state.token}", Toast.LENGTH_LONG).show()

                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }
}