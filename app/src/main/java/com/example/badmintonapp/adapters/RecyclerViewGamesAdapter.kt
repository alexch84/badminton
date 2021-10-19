package com.example.badmintonapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badmintonapp.R
import com.example.badmintonapp.data.Game
import kotlinx.android.synthetic.main.game_item.view.*

class RecyclerViewGamesAdapter() : RecyclerView.Adapter<RecyclerViewGamesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewGamesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewGamesAdapter.ViewHolder, position: Int) {
        val game = differ.currentList[position]

        holder.itemView.apply {
            score.text = context.getString(R.string.score, game.alexScore, game.yuriyScore)
            date.text = game.date
        }
    }


}