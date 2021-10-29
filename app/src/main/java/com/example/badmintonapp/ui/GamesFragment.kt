package com.example.badmintonapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badmintonapp.R
import com.example.badmintonapp.adapters.RecyclerViewGamesAdapter
import com.example.badmintonapp.data.Game
import com.example.badmintonapp.databinding.FragmentGamesBinding
import com.example.badmintonapp.viewModel.GamesViewModel

class GamesFragment : Fragment(R.layout.fragment_games) {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var gamesAdapter: RecyclerViewGamesAdapter

    private var gamesList: MutableList<Game> = ArrayList()

    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gamesViewModel = ViewModelProvider(this).get(GamesViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGamesBinding.bind(view)

        setUpGamesRecyclerView()

        gamesViewModel.fetchGames()
        gamesViewModel.getRealtimeUpdates()
        binding.progressBar.visibility = View.VISIBLE

        gamesViewModel.games.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            Log.d("list", it.toString())

            gamesList.clear()
            gamesList.addAll(it)

            gamesAdapter.differ.submitList(gamesList)
            gamesAdapter.notifyDataSetChanged()

        })

        gamesViewModel.game.observe(viewLifecycleOwner, Observer {
            gamesList.add(it)
            gamesAdapter.differ.submitList(gamesList)
            gamesAdapter.notifyDataSetChanged()
        })

        binding.addGameButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_gamesFragment_to_addGameDialogFragment)
        }

    }

    private fun setUpGamesRecyclerView() {
        gamesAdapter = RecyclerViewGamesAdapter()
        binding.gamesRecyclerView.apply {
            adapter = gamesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}