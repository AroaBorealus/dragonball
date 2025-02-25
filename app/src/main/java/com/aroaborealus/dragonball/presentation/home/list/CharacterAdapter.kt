package com.aroaborealus.dragonball.presentation.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aroaborealus.dragonball.model.Character
import com.bumptech.glide.Glide
import com.aroaborealus.dragonball.R
import com.aroaborealus.dragonball.databinding.ItemCharacterBinding

class CharacterAdapter(
    private var onCharacterClicked: (Character) -> Unit,
): RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characters = listOf<Character>()

    fun updateCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private var onCharacterClciked: (Character) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.tvNombre.text = character.nombre
            Glide
                .with(binding.root)
                .load(character.imagenUrl)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            binding.root.setOnClickListener {
                onCharacterClciked(character)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onCharacterClciked = onCharacterClicked,
        )
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }
}