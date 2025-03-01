package com.aroaborealus.dragonball.presentation.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aroaborealus.dragonball.R
import com.aroaborealus.dragonball.databinding.ItemCharacterBinding
import com.aroaborealus.dragonball.model.Character

class CharacterAdapter(
    private var onPersonajeClicked: (Character) -> Unit,
): RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var personajes = listOf<Character>()

    fun actualizarPersonajes(personajes: List<Character>) {
        this.personajes = personajes
        notifyDataSetChanged()
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private var onPersonajeClicked: (Character) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(personaje: Character) {
            binding.tvNombre.text = personaje.nombre
            Glide
                .with(binding.root)
                .load(personaje.imagenUrl)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            binding.pbVida.max = personaje.vidaTotal
            binding.pbVida.progress = personaje.vidaActual

            if (personaje.vidaActual <= 0) {
                binding.root.setBackgroundColor(binding.root.context.getColor(R.color.light_gray))
                binding.root.isClickable = false
                binding.root.isEnabled = false
            } else {
                binding.root.setBackgroundColor(binding.root.context.getColor(R.color.light_orange))
                binding.root.isClickable = true
                binding.root.isEnabled = true
            }

            binding.root.setOnClickListener {
                if (binding.root.isEnabled) {
                    onPersonajeClicked(personaje)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPersonajeClicked = onPersonajeClicked,
        )
    }

    override fun getItemCount(): Int {
        return personajes.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(personajes[position])
    }
}