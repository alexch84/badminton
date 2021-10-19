package com.example.badmintonapp.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.badmintonapp.R
import com.example.badmintonapp.databinding.FragmentTotalBinding
import com.example.badmintonapp.viewModel.GamesViewModel


class TotalFragment : Fragment(R.layout.fragment_total) {

    private var _binding: FragmentTotalBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTotalBinding.bind(view)

        gamesViewModel.fetchGames()
        binding.progressBar.visibility = View.VISIBLE

        gamesViewModel.games.observe(viewLifecycleOwner, Observer { games ->
            binding.progressBar.visibility = View.GONE

            games?.let {
                var scoreAlex = 0
                var scoreYuri = 0

                for (game in games) {
                    scoreAlex += game.alexScore!!
                    scoreYuri += game.yuriyScore!!
                }

                when {
                    scoreAlex > scoreYuri -> {
                        binding.scoreAlex.setTextColor(Color.parseColor("#008000"))
                        binding.scoreYuri.setTextColor(Color.parseColor("#ff0000"))
                    }
                    scoreAlex < scoreYuri -> {
                        binding.scoreAlex.setTextColor(Color.parseColor("#ff0000"))
                        binding.scoreYuri.setTextColor(Color.parseColor("#008000"))
                    }
                    else -> {
                        binding.scoreAlex.setTextColor(Color.parseColor("#808080"))
                        binding.scoreYuri.setTextColor(Color.parseColor("#808080"))
                    }
                }

                binding.scoreAlex.text = scoreAlex.toString()
                binding.scoreYuri.text = scoreYuri.toString()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}