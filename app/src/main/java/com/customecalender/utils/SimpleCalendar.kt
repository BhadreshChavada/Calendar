package com.customecalender.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import com.customecalender.R
import com.customecalender.TripBreakdownData
import com.customecalender.databinding.LayoutDayBinding
import com.customecalender.databinding.TripBreakdownBarBinding
import com.customecalender.databinding.TripBreakdownTitleBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


class SimpleCalendar : LinearLayout {
    private var currentDate: TextView? = null
    private var currentMonth: TextView? = null
    private var selectedDayButton: Button? = null
    private lateinit var days: Array<LayoutDayBinding?>
    var llRoot: LinearLayout? = null
    var weekOneLayout: LinearLayout? = null
    var weekTwoLayout: LinearLayout? = null
    var weekThreeLayout: LinearLayout? = null
    var weekFourLayout: LinearLayout? = null
    var weekFiveLayout: LinearLayout? = null
    var weekSixLayout: LinearLayout? = null
    private lateinit var weeks: Array<LinearLayout?>
    private var currentDateDay = 0
    private var chosenDateDay = 0
    private var currentDateMonth = 0
    private var chosenDateMonth = 0
    private var currentDateYear = 0
    private var chosenDateYear = 0
    private var pickedDateDay = 0
    private var pickedDateMonth = 0
    private var pickedDateYear = 0
    var userMonth = 0
    var userYear = 0
    private var mListener: DayClickListener? = null
    private var userDrawable: Drawable? = null
    private var calendar: Calendar? = null
    var defaultButtonParams: LayoutParams? = null
    private var userButtonParams: LayoutParams? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        val metrics = resources.displayMetrics
        val view = LayoutInflater.from(context).inflate(R.layout.simple_calendar, this, true)
        calendar = Calendar.getInstance()
        llRoot = view.findViewById(R.id.llRoot) as LinearLayout
        weekOneLayout = view.findViewById(R.id.calendar_week_1) as LinearLayout
        weekTwoLayout = view.findViewById(R.id.calendar_week_2) as LinearLayout
        weekThreeLayout = view.findViewById(R.id.calendar_week_3) as LinearLayout
        weekFourLayout = view.findViewById(R.id.calendar_week_4) as LinearLayout
        weekFiveLayout = view.findViewById(R.id.calendar_week_5) as LinearLayout
        weekSixLayout = view.findViewById(R.id.calendar_week_6) as LinearLayout
        currentMonth = view.findViewById(R.id.current_month) as TextView

        getDaysWidths()

        chosenDateDay = calendar?.get(Calendar.DAY_OF_MONTH)!!

        if (userMonth != 0 && userYear != 0) {
            chosenDateMonth = userMonth
            currentDateMonth = chosenDateMonth
            chosenDateYear = userYear
            currentDateYear = chosenDateYear
        } else {
            chosenDateMonth = calendar?.get(Calendar.MONTH)!!
            currentDateMonth = chosenDateMonth
            chosenDateYear = calendar?.get(Calendar.YEAR)!!
            currentDateYear = chosenDateYear
        }
        currentMonth!!.text = ENG_MONTH_NAMES[currentDateMonth]
        initializeDaysWeeks()
        defaultButtonParams = if (userButtonParams != null) {
            userButtonParams
        } else {
            getdaysLayoutParams()
        }

        addDaysinCalendar(defaultButtonParams, context, metrics)
        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay)
    }

    private fun initializeDaysWeeks() {
        weeks = arrayOfNulls(6)
        days = arrayOfNulls(6 * 7)
        weeks[0] = weekOneLayout
        weeks[1] = weekTwoLayout
        weeks[2] = weekThreeLayout
        weeks[3] = weekFourLayout
        weeks[4] = weekFiveLayout
        weeks[5] = weekSixLayout
    }

    private fun initCalendarWithDate(year: Int, month: Int, day: Int) {
        if (calendar == null) calendar = Calendar.getInstance()
        calendar!![year, month] = day
        val daysInCurrentMonth = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        chosenDateYear = year
        chosenDateMonth = month
        chosenDateDay = day
        calendar!![year, month] = 0
        val firstDayOfCurrentMonth = calendar!![Calendar.DAY_OF_WEEK]
        calendar!![year, month] = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        var dayNumber = 1
        var daysLeftInFirstWeek = 0
        var indexOfDayAfterLastDayOfMonth = 0
        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth
            for (i in firstDayOfCurrentMonth until firstDayOfCurrentMonth + daysInCurrentMonth) {
                val dayView = days[i]
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    dayView!!.root.setBackgroundColor(resources.getColor(R.color.pink))
                    dayView.tvDay.setTextColor(Color.WHITE)
                } else {
                    dayView!!.tvDay.setTextColor(Color.BLACK)
//                    dayView.setBackgroundColor(Color.TRANSPARENT)
                }
                val dateArr = IntArray(3)
                dateArr[0] = dayNumber
                dateArr[1] = chosenDateMonth
                dateArr[2] = chosenDateYear
                dayView.root.tag = dateArr
                dayView.tvDay.text = dayNumber.toString()
                dayView.root.setOnClickListener { v -> onDayClick(v) }
                ++dayNumber
            }
        } else {
            daysLeftInFirstWeek = 8
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth
            for (i in 8 until 8 + daysInCurrentMonth) {
                val dayView = days[i]
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    dayView!!.root.setBackgroundColor(resources.getColor(R.color.pink))
                    dayView.tvDay.setTextColor(Color.WHITE)
                } else {
                    dayView!!.tvDay.setTextColor(Color.BLACK)
//                    dayView.setBackgroundColor(Color.TRANSPARENT)
                }
                val dateArr = IntArray(3)
                dateArr[0] = dayNumber
                dateArr[1] = chosenDateMonth
                dateArr[2] = chosenDateYear
                dayView.root.tag = dateArr
                dayView.tvDay.text = dayNumber.toString()
                dayView.root.setOnClickListener { v -> onDayClick(v) }
                ++dayNumber
            }
        }
        if (month > 0) calendar!![year, month - 1] = 1 else calendar!![year - 1, 11] = 1
        var daysInPreviousMonth = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in daysLeftInFirstWeek - 1 downTo 0) {
            val dayView = days[i]
            val dateArr = IntArray(3)
            if (chosenDateMonth > 0) {
                if (currentDateMonth == chosenDateMonth - 1 && currentDateYear == chosenDateYear && daysInPreviousMonth == currentDateDay) {
                } else {
//                    dayView.root.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = daysInPreviousMonth
                dateArr[1] = chosenDateMonth - 1
                dateArr[2] = chosenDateYear
            } else {
                if (currentDateMonth == 11 && currentDateYear == chosenDateYear - 1 && daysInPreviousMonth == currentDateDay) {
                } else {
//                    dayView.root.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = daysInPreviousMonth
                dateArr[1] = 11
                dateArr[2] = chosenDateYear - 1
            }
            dayView!!.root.tag = dateArr
            dayView.tvDay.text = daysInPreviousMonth--.toString()
            dayView.root.setOnClickListener { v -> onDayClick(v) }
        }
        var nextMonthDaysCounter = 1
        for (i in indexOfDayAfterLastDayOfMonth until days.size) {
            val dayView = days[i]
            val dateArr = IntArray(3)
            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1 && currentDateYear == chosenDateYear && nextMonthDaysCounter == currentDateDay) {
                    dayView!!.root.setBackgroundColor(resources.getColor(R.color.pink))
                } else {
//                    dayView.root.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = nextMonthDaysCounter
                dateArr[1] = chosenDateMonth + 1
                dateArr[2] = chosenDateYear
            } else {
                if (currentDateMonth == 0 && currentDateYear == chosenDateYear + 1 && nextMonthDaysCounter == currentDateDay) {
                    dayView!!.root.setBackgroundColor(resources.getColor(R.color.pink))
                } else {
//                    dayView.root.setBackgroundColor(Color.TRANSPARENT)
                }
                dateArr[0] = nextMonthDaysCounter
                dateArr[1] = 0
                dateArr[2] = chosenDateYear + 1
            }
            dayView!!.root.tag = dateArr
            dayView.tvDay.setTextColor(Color.parseColor(CUSTOM_GREY))
            dayView.tvDay.text = nextMonthDaysCounter++.toString()
            dayView.root.setOnClickListener { v -> onDayClick(v) }
        }
        calendar!![chosenDateYear, chosenDateMonth] = chosenDateDay

    }

    fun onDayClick(view: View?) {
        /*mListener!!.onDayClick(view)
        if (selectedDayButton != null) {
            if (chosenDateYear == currentDateYear && chosenDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
                selectedDayButton!!.setBackgroundColor(resources.getColor(R.color.pink))
                selectedDayButton!!.setTextColor(Color.WHITE)
            } else {
                selectedDayButton!!.setBackgroundColor(Color.TRANSPARENT)
                if (selectedDayButton!!.currentTextColor != Color.RED) {
                    selectedDayButton!!.setTextColor(
                        resources
                            .getColor(R.color.calendar_number)
                    )
                }
            }
        }
        selectedDayButton = view as Button?
        if (selectedDayButton!!.tag != null) {
            val dateArray = selectedDayButton!!.tag as IntArray
            pickedDateDay = dateArray[0]
            pickedDateMonth = dateArray[1]
            pickedDateYear = dateArray[2]
        }
        if (pickedDateYear == currentDateYear && pickedDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
            selectedDayButton!!.setBackgroundColor(resources.getColor(R.color.pink))
            selectedDayButton!!.setTextColor(Color.WHITE)
        } else {
            selectedDayButton!!.setBackgroundColor(resources.getColor(R.color.grey))
            if (selectedDayButton!!.currentTextColor != Color.RED) {
                selectedDayButton!!.setTextColor(Color.WHITE)
            }
        }*/
    }

    var  daysWidth = 0
    private fun addDaysinCalendar(
        buttonParams: LayoutParams?, context: Context,
        metrics: DisplayMetrics
    ) {
        var engDaysArrayCounter = 0
        for (weekNumber in 0..5) {
            for (dayInWeek in 0..6) {
                val day = LayoutDayBinding.inflate(LayoutInflater.from(context), this, false)
                day.root.layoutParams = buttonParams
                days[engDaysArrayCounter] = day
                weeks[weekNumber]!!.addView(day.root)
                ++engDaysArrayCounter
            }
        }
    }

    private fun getDaysWidths() {
        val vto: ViewTreeObserver = llRoot!!.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                llRoot!!.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                 daysWidth = llRoot!!.getMeasuredWidth()
            }
        })
    }

    fun textBar(marginStart :Int, week:Int,text:String) {
        var constrainLayout : ConstraintLayout
        when(week){
            1 -> constrainLayout = findViewById(R.id.constrain_week_1)
            2-> constrainLayout = findViewById(R.id.constrain_week_2)
            3-> constrainLayout = findViewById(R.id.constrain_week_3)
            4-> constrainLayout = findViewById(R.id.constrain_week_4)
            5-> constrainLayout = findViewById(R.id.constrain_week_5)
            6-> constrainLayout = findViewById(R.id.constrain_week_6)
            else -> constrainLayout = findViewById(R.id.constrain_week_1)
        }
        val binding = TripBreakdownTitleBinding.inflate(LayoutInflater.from(context), this, false)
        binding.tvTripBreakdown.text = text
        val intArray = IntArray(2)
        days[3]?.root?.doOnLayout {
            it.getLocationOnScreen(intArray)
            Log.d("width", intArray[0].toString())
        }
        val layoutParams = getTextBarParams()
        binding.tvTripBreakdown.layoutParams = layoutParams
        layoutParams.startToStart = constrainLayout.id
        layoutParams.endToEnd = constrainLayout.id
        layoutParams.topToTop = constrainLayout.id
        layoutParams.marginStart = marginStart
        layoutParams.topMargin = 100
        constrainLayout.addView(binding.tvTripBreakdown)

    }

    private fun multiColorDrawBar (
        marginStart: Int,
        marginEnd: Int,
        week: Int,
        array: ArrayList<TripBreakdownData>,
        singleColor: Boolean, ) {
        var constrainLayout : ConstraintLayout
        when(week){
            1 -> constrainLayout = findViewById(R.id.constrain_week_1)
            2-> constrainLayout = findViewById(R.id.constrain_week_2)
            3-> constrainLayout = findViewById(R.id.constrain_week_3)
            4-> constrainLayout = findViewById(R.id.constrain_week_4)
            5-> constrainLayout = findViewById(R.id.constrain_week_5)
            6-> constrainLayout = findViewById(R.id.constrain_week_6)
            else -> constrainLayout = findViewById(R.id.constrain_week_1)
        }

        val binding = TripBreakdownBarBinding.inflate(LayoutInflater.from(context), this, false)
        val layoutParams = getBarParams()
        binding.llTripBreakdown.layoutParams = layoutParams
//        binding.llTripBreakdown.weightSum = 1f
        layoutParams.startToStart = constrainLayout.id
        layoutParams.endToEnd = constrainLayout.id
        layoutParams.topToTop = constrainLayout.id
        layoutParams.marginStart = marginStart
        layoutParams.topMargin = 150
        layoutParams.marginEnd = marginEnd
        constrainLayout.addView(binding.llTripBreakdown)

        for (i in array) {
            val view = View(context);
            val lparams = LayoutParams(0, LayoutParams.WRAP_CONTENT,
                (i.Percentage).toFloat()
            )
            view.layoutParams = lparams
            if(singleColor){
                view.setBackgroundColor(Color.GREEN)
            }else{
                view.setBackgroundColor(getColor(i.BreakdownType))
            }

            binding.llTripBreakdown.addView(view)
        }
    }

    fun getColor(index : Int): Int {
        return when(index){
            1 -> {
                Color.RED
            }
            2 -> {
                Color.BLUE
            }
            3 -> {
                Color.GREEN
            }
            4 -> {
                Color.CYAN
            }
            5 -> {
                Color.DKGRAY
            }
            6 -> {
                Color.GRAY
            }
            else -> {
                Color.BLACK
            }
        }
    }
    private fun getBarParams(): ConstraintLayout.LayoutParams {
        return ConstraintLayout.LayoutParams(0, 30)
    }

    private fun getTextBarParams(): ConstraintLayout.LayoutParams {
        return ConstraintLayout.LayoutParams(0, 40)
    }

    private fun getdaysLayoutParams(): LayoutParams {
        val buttonParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            250
        )
        buttonParams.weight = 1f
        return buttonParams
    }

    fun setUserDaysLayoutParams(userButtonParams: LayoutParams?) {
        this.userButtonParams = userButtonParams
    }

    fun setUserCurrentMonthYear(userMonth: Int, userYear: Int) {
        this.userMonth = userMonth
        this.userYear = userYear
    }

    fun setDayBackground(userDrawable: Drawable?) {
        this.userDrawable = userDrawable
    }

    interface DayClickListener {
        fun onDayClick(view: View?)
    }

    fun setCallBack(mListener: DayClickListener?) {
        this.mListener = mListener
    }

    companion object {
        private const val CUSTOM_GREY = "#a0a0a0"
        private val ENG_MONTH_NAMES = arrayOf(
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        )

    }

    fun drawLines(
        calenderView: SimpleCalendar?,
        startDateString: String,
        endDateString: String,
        startDateTitle: String,
        array: ArrayList<TripBreakdownData>,
        singleColor:Boolean = false
    ){
        calenderView?.post {


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
                    val width = (this.width ?: 1) / 7
                    for (i in startDateWeekOfMonth..endDateWeekOfMonth) {
                        if(weekStartDay == 7){
                            weekStartDay=0
                        }
                        if (i == startDateWeekOfMonth) {
                            this.multiColorDrawBar(
                                width * weekStartDay,
                                0,
                                startDateWeekOfMonth,
                                array,
                                singleColor
                            )
                        } else if (i == endDateWeekOfMonth) {
                            this.multiColorDrawBar(
                                0,
                                width * (6 - weekEndDay),
                                endDateWeekOfMonth,
                                array,
                                singleColor
                            )
                        } else {
                            this.multiColorDrawBar(
                                0,
                                0,
                                i,
                                array,
                                singleColor
                            )
                        }
                    }
                    if (startDateTitle.isNotEmpty()) {
                        this.textBar(
                            width * weekStartDay,
                            startDateWeekOfMonth,
                            startDateTitle
                        )
                    }

                } else {
                    val width = (this.width ?: 1) / 7
                    if(weekStartDay == 7){
                        weekStartDay=0
                    }
                    this.multiColorDrawBar(
                        width * weekStartDay,
                        width * (6 - weekEndDay),
                        startDateWeekOfMonth,
                        array,
                        singleColor
                    )

                    if (startDateTitle.isNotEmpty()) {
                        this.textBar(
                            width * weekStartDay,
                            startDateWeekOfMonth,
                            startDateTitle
                        )
                    }
                }
            }
        }
    }
}