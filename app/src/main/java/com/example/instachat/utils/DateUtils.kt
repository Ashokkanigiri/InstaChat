package com.example.instachat.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val format = SimpleDateFormat("MM/dd/yyyy HH:mm:sss")

    fun getCurrentTimeInMillis(): String? {
        return format.format(Date())
    }

    fun getCurrentTime(): String {
        return System.currentTimeMillis().toString()
    }

    fun isToday(date: String): Boolean {
//        val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss")
        val day = SimpleDateFormat("d").format(Date())
        val month = SimpleDateFormat("MM").format(Date())
        val year = SimpleDateFormat("YYYY").format(Date())

        val day1 = SimpleDateFormat("d").format(format.parse(date))
        val month1 = SimpleDateFormat("MM").format(format.parse(date))
        val year1 = SimpleDateFormat("YYYY").format(format.parse(date))

        val yesterdayCalender = Calendar.getInstance()
        yesterdayCalender.set(Calendar.DAY_OF_MONTH, day1.toInt())
        yesterdayCalender.set(Calendar.MONTH, month1.toInt() - 1)
        yesterdayCalender.set(Calendar.YEAR, year1.toInt())

        val todayCalender = Calendar.getInstance()
        todayCalender.set(Calendar.DAY_OF_MONTH, day.toInt())
        todayCalender.set(Calendar.MONTH, month.toInt() - 1)
        todayCalender.set(Calendar.YEAR, year.toInt())
        return getDiff(todayCalender, yesterdayCalender) == 0
    }

    fun isYesterday(date: String): Boolean {
//        val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss")
        val day = SimpleDateFormat("d").format(Date())
        val month = SimpleDateFormat("MM").format(Date())
        val year = SimpleDateFormat("YYYY").format(Date())

        val day1 = SimpleDateFormat("d").format(format.parse(date))
        val month1 = SimpleDateFormat("MM").format(format.parse(date))
        val year1 = SimpleDateFormat("YYYY").format(format.parse(date))

        val yesterdayCalender = Calendar.getInstance()
        yesterdayCalender.set(Calendar.DAY_OF_MONTH, day1.toInt())
        yesterdayCalender.set(Calendar.MONTH, month1.toInt() - 1)
        yesterdayCalender.set(Calendar.YEAR, year1.toInt())

        val todayCalender = Calendar.getInstance()
        todayCalender.set(Calendar.DAY_OF_MONTH, day.toInt())
        todayCalender.set(Calendar.MONTH, month.toInt() - 1)
        todayCalender.set(Calendar.YEAR, year.toInt())
        return getDiff(todayCalender, yesterdayCalender) == 1
    }

    fun isDateTwoDaysAgo(date: String): Boolean {
//        val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss")
        val day = SimpleDateFormat("d").format(Date())
        val month = SimpleDateFormat("MM").format(Date())
        val year = SimpleDateFormat("YYYY").format(Date())

        val day1 = SimpleDateFormat("d").format(format.parse(date))
        val month1 = SimpleDateFormat("MM").format(format.parse(date))
        val year1 = SimpleDateFormat("YYYY").format(format.parse(date))

        val yesterdayCalender = Calendar.getInstance()
        yesterdayCalender.set(Calendar.DAY_OF_MONTH, day1.toInt())
        yesterdayCalender.set(Calendar.MONTH, month1.toInt() - 1)
        yesterdayCalender.set(Calendar.YEAR, year1.toInt())

        val todayCalender = Calendar.getInstance()
        todayCalender.set(Calendar.DAY_OF_MONTH, day.toInt())
        todayCalender.set(Calendar.MONTH, month.toInt() - 1)
        todayCalender.set(Calendar.YEAR, year.toInt())
        return getDiff(todayCalender, yesterdayCalender) > 2
    }

    private fun getDiff(calone: Calendar, caltwo: Calendar ): Int{
        return ((calone.timeInMillis - caltwo.timeInMillis ) /(1000 *60 *60 *24)).toInt()

    }
}