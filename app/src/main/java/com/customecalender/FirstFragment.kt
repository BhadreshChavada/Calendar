package com.customecalender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.customecalender.databinding.FragmentFirstBinding

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

        val array = arrayListOf<TripBreakdownData>()
        array.add(TripBreakdownData(1, 0.10))
        array.add(TripBreakdownData(3, 0.50))
        array.add(TripBreakdownData(4, 0.15))
        array.add(TripBreakdownData(6, 0.10))
        array.add(TripBreakdownData(1, 0.15))

        _binding?.squareDay?.drawLines(
            _binding?.squareDay,
            "2023-09-04",
            "2023-09-07",
            "Multiple Color",
            array
        )

        _binding?.squareDay?.drawLines(
            _binding?.squareDay,
            "2023-09-15",
            "2023-09-19",
            "Single Color",
            array,
            true
        )



    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
