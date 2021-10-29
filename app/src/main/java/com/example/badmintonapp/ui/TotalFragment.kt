package com.example.badmintonapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.badmintonapp.R
import com.example.badmintonapp.databinding.FragmentTotalBinding
import com.example.badmintonapp.viewModel.GamesViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


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

                var colorAlex = ContextCompat.getColor(requireContext(), R.color.grey)
                var colorYuri = ContextCompat.getColor(requireContext(), R.color.grey)

                for (game in games) {
                    scoreAlex += game.alexScore!!
                    scoreYuri += game.yuriyScore!!
                }

                when {
                    scoreAlex > scoreYuri -> {
                        colorAlex = ContextCompat.getColor(requireContext(), R.color.green)
                        colorYuri = ContextCompat.getColor(requireContext(), R.color.red)
                    }
                    scoreAlex < scoreYuri -> {
                        colorAlex = ContextCompat.getColor(requireContext(), R.color.red)
                        colorYuri = ContextCompat.getColor(requireContext(), R.color.green)
                    }

                }

                binding.scoreAlex.setTextColor(colorAlex)
                binding.scoreYuri.setTextColor(colorYuri)
                binding.scoreAlex.text = scoreAlex.toString()
                binding.scoreYuri.text = scoreYuri.toString()

                setChartData(scoreAlex, scoreYuri, colorAlex, colorYuri)
            }
        })
    }

    private fun setChartData(scoreAlex: Int, scoreYuri: Int, colorAlex: Int, colorYuri: Int) {
        val barEntryAlex = ArrayList<BarEntry>()
        barEntryAlex.add(BarEntry(0f, scoreAlex.toFloat()))

        val barEntryEmpty = ArrayList<BarEntry>()
        barEntryEmpty.add(BarEntry(1f, 0f))

        val barEntryYuri = ArrayList<BarEntry>()
        barEntryYuri.add(BarEntry(2f, scoreYuri.toFloat()))


        val dataSetAlex = BarDataSet(barEntryAlex, "Александр")
        dataSetAlex.color = colorAlex

        val dataSetEmpty = BarDataSet(barEntryEmpty, "")
        dataSetEmpty.color = Color.parseColor("#ffffff")

        val dataSetYuri = BarDataSet(barEntryYuri, "Юрий")
        dataSetYuri.color = colorYuri

        val barDataSets = ArrayList<BarDataSet>()
        barDataSets.add(dataSetAlex)
        barDataSets.add(dataSetEmpty)
        barDataSets.add(dataSetYuri)

        val lineData = BarData(barDataSets as List<IBarDataSet>?)
        binding.chart.data = lineData

        var description = binding.chart.description
        description.isEnabled = false

        binding.chart.setFitBars(true)

        val left = binding.chart.axisLeft
        left.setDrawLabels(false)

        val xAxis = binding.chart.xAxis
        xAxis.setDrawLabels(false)

        val right = binding.chart.axisRight
        right.setDrawLabels(false)

        binding.chart.animateXY(2000, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}