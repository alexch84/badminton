package com.example.badmintonapp.ui

import android.app.DatePickerDialog
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.badmintonapp.R
import com.example.badmintonapp.data.Game
import com.example.badmintonapp.databinding.FragmentAddGameDialogBinding
import com.example.badmintonapp.viewModel.GamesViewModel
import java.util.*


class AddGameDialogFragment : DialogFragment() {

    private var _binding: FragmentAddGameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gamesViewModel = ViewModelProvider(this).get(GamesViewModel::class.java)
        _binding = FragmentAddGameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamesViewModel.result.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE

            val message = if (it == null) {
                "Игра добавлена"
            } else {
                "Ошибка добавления игры ${it.message}"
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            dismiss()
        })

        binding.saveButton.setOnClickListener {
            val alexScore = binding.alexEditText.text?.toString()?.trim()
            val yuriyScore = binding.yuriyEditText.text?.toString()?.trim()
            val date = binding.dateEditText.text?.toString()?.trim()

            if (alexScore.isNullOrEmpty()) {
                binding.alexInputLayout.error = "Обязательное поле"
                return@setOnClickListener
            }

            if (yuriyScore.isNullOrEmpty()) {
                binding.yuriyInputLayout.error = "Обязательное поле"
                return@setOnClickListener
            }

            if (date.isNullOrEmpty()) {
                binding.dateInputLayout.error = "Обязательное поле"
                return@setOnClickListener
            }

            val game = Game()
            game.alexScore = alexScore.toInt()
            game.yuriyScore = yuriyScore.toInt()
            game.date = date

            gamesViewModel.addGame(game)

            binding.progressBar.visibility = View.VISIBLE
        }

        binding.dateEditText.setOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.MyDatePicker,
            { view, year, month, dayOfMonth ->
                val m: String = if (month < 9) {
                    "0" + (month + 1)
                } else {
                    "" + (month + 1)
                }
                val d: String = if (dayOfMonth < 9) {
                    "0$dayOfMonth"
                } else {
                    "" + dayOfMonth
                }
                val date = "$d.$m.$year"

                binding.dateEditText.setText(date)

            }, year, month, day
        )

        datePickerDialog.show()
    }

    override fun onResume() {
        // Store access variables for window and blank point
        val window: Window? = dialog!!.window
        val size = Point()
        // Store dimensions of the screen in `size`
        val display: Display? = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        // Set the width of the dialog proportional to 95% of the screen width
        window?.setLayout((size.x * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        // Call super onResume after sizing
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}