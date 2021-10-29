package com.example.badmintonapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badmintonapp.R
import com.example.badmintonapp.data.Game
import com.example.badmintonapp.databinding.GameItemBinding

class RecyclerViewGamesAdapter() : RecyclerView.Adapter<RecyclerViewGamesAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(val binding: GameItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewGamesAdapter.ViewHolder, position: Int) {
        val game = differ.currentList[position]

        holder.binding.apply {
            this.score.text = context.getString(R.string.score, game.alexScore, game.yuriyScore)
            this.date.text = game.date
        }
    }


}