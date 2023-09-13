package com.customecalender

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.customecalender.databinding.FragmentFirstBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

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

        drawLines("2023-09-04","2023-09-07","Test", array)
//        drawLines("2023-09-03","2023-09-13","Hello")


    }

    fun drawLines(
        startDateString: String,
        endDateString: String,
        startDateTitle: String,
        array: ArrayList<TripBreakdownData>){
        _binding?.squareDay?.post {


            var weekStartDay =1
            var weekEndDay =1

            val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(startDateString);
            val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(endDateString);

            val calendarInstance = Calendar.getInstance()
            calendarInstance.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

            calendarInstance.timeInMillis = startDate.time
            val startDateWeekOfMonth = calendarInstance.get(Calendar.WEEK_OF_MONTH)

            calendarInstance.timeInMillis = endDate.time
            val endDateWeekOfMonth = calendarInstance.get(Calendar.WEEK_OF_MONTH)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                weekStartDay = LocalDate.parse(startDateString).dayOfWeek.value
                weekEndDay = LocalDate.parse(endDateString).dayOfWeek.value

                if (startDateWeekOfMonth != endDateWeekOfMonth) {
                    val width = (_binding?.squareDay?.width ?: 1) / 7
                    for (i in startDateWeekOfMonth..endDateWeekOfMonth) {
                        if(weekStartDay == 7){
                            weekStartDay=0
                        }
                        if (i == startDateWeekOfMonth) {
                            _binding?.squareDay?.multiColorDrawBar(
                                width * weekStartDay,
                                0,
                                Color.GREEN,
                                startDateWeekOfMonth
                            )
                        } else if (i == endDateWeekOfMonth) {
                            _binding?.squareDay?.multiColorDrawBar(
                                0,
                                width * (6 - weekEndDay),
                                Color.GREEN,
                                endDateWeekOfMonth
                            )
                        } else {
                            _binding?.squareDay?.multiColorDrawBar(
                                0,
                                0,
                                Color.GREEN,
                                i
                            )
                        }
                    }
                    if (startDateTitle.isNotEmpty()) {
                        _binding?.squareDay?.textBar(
                            width * weekStartDay,
                            startDateWeekOfMonth,
                            startDateTitle
                        )
                    }

                } else {
                    val width = (_binding?.squareDay?.width ?: 1) / 7
                    if(weekStartDay == 7){
                        weekStartDay=0
                    }
                    _binding?.squareDay?.multiColorDrawBar(
                        width * weekStartDay,
                        width * (6 - weekEndDay),
                        Color.GREEN,
                        startDateWeekOfMonth
                    )

                    if (startDateTitle.isNotEmpty()) {
                        _binding?.squareDay?.textBar(
                            width * weekStartDay,
                            startDateWeekOfMonth,
                            startDateTitle
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}