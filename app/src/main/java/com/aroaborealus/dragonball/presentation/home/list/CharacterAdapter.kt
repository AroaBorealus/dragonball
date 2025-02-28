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
): RecyclerView.Adapter<CharacterAdapter.PersonajeViewHolder>() {

    private var personajes = listOf<Character>()

    fun actualizarPersonajes(personajes: List<Character>) {
        this.personajes = personajes
        notifyDataSetChanged()
    }

    class PersonajeViewHolder(
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
            binding.root.setOnClickListener {
                onPersonajeClicked(personaje)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeViewHolder {
        return PersonajeViewHolder(
            binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onPersonajeClicked = onPersonajeClicked,
        )
    }

    override fun getItemCount(): Int {
        return personajes.size
    }

    override fun onBindViewHolder(holder: PersonajeViewHolder, position: Int) {
        holder.bind(personajes[position])
    }


}