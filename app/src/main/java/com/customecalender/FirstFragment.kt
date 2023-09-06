package com.customecalender

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.customecalender.databinding.FragmentFirstBinding
import java.time.LocalDate

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val startDate =
//        val pairingData = PairingsData()



        _binding?.squareDay?.post {


            var weekStartDay =1
            var weekEndDay =1
            var weekOfMonth = 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                weekStartDay = LocalDate.parse("2023-09-05").dayOfWeek.value
                weekEndDay = LocalDate.parse("2023-09-08").dayOfWeek.value
            }

            val width = (_binding?.squareDay?.width ?: 1) / 7
            _binding?.squareDay?.multiColorDrawBar(width*weekStartDay,width*(7-weekEndDay), Color.GREEN,weekOfMonth)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}