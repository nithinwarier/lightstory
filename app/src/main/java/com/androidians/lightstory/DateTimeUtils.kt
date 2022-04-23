package com.androidians.lightstory

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DateTimeUtils {

    private const val DAYS_IN_MILLIS = 1000 * 60 * 60 * 24
    private const val HOURS_IN_MILLIS = 1000 * 60 * 60
    private const val MINUTE_IN_MILLIS = 1000 * 60

    fun getTimeDifferenceInHHandMM(startTime: String, endTime: String): ArrayList<Long> {
        val time = ArrayList<Long>()
        val outFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val startDate = outFormat.parse(startTime)
            val endDate = outFormat.parse(endTime)
            var difference = endDate.time - startDate.time
            if (difference < 0) {
                time.add(0L) // for yesterday
                val dateMax = outFormat.parse("24:00")
                val dateMin = outFormat.parse("00:00")
                difference = dateMax.time - startDate.time + (endDate.time - dateMin.time)
            } else {
                time.add(1L) // for today
            }
            val days = (difference / (DAYS_IN_MILLIS)).toInt()
            val hours = ((difference - DAYS_IN_MILLIS * days) / (HOURS_IN_MILLIS))
            val minutes = (difference - (DAYS_IN_MILLIS * days) - HOURS_IN_MILLIS * hours) / (MINUTE_IN_MILLIS)
            time.add(hours)
            time.add(minutes)
            time
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date difference parse exception: startTime: $startTime endTime: $endTime")
        }
    }

    fun getTimeDifferenceInMinutes(startTime: Long, endTime: Long): Long {
        val outFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val cal1 = Calendar.getInstance()
            cal1.timeInMillis = startTime
            val cal2 = Calendar.getInstance()
            cal2.timeInMillis = endTime
            var difference = endTime - startTime
            if (difference < 0) {
                val dateMax = outFormat.parse("24:00")
                val dateMin = outFormat.parse("00:00")
                difference = dateMax.time - cal1.time.time + (cal2.time.time - dateMin.time)
            }

            val days = difference / DAYS_IN_MILLIS
            val hours = (difference - (DAYS_IN_MILLIS * days)) / (HOURS_IN_MILLIS)
            val minutes = (difference - (DAYS_IN_MILLIS * days) - (HOURS_IN_MILLIS * hours)) / (MINUTE_IN_MILLIS)
            (hours * 60) + minutes
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date difference parse exception: startTime: $startTime endTime: $endTime")
        }
    }

    fun getTimeDifferenceInHHmm(startTime: Long, endTime: Long): ArrayList<Long> {
        val time = ArrayList<Long>()
        val outFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val cal1 = Calendar.getInstance()
            cal1.timeInMillis = startTime
            val cal2 = Calendar.getInstance()
            cal2.timeInMillis = endTime
            var difference = endTime - startTime
            if (difference < 0) {
                val dateMax = outFormat.parse("24:00")
                val dateMin = outFormat.parse("00:00")
                difference = dateMax.time - cal1.time.time + (cal2.time.time - dateMin.time)
                time.add(1)
            } else {
                time.add(0)
            }

            val days = difference / (DAYS_IN_MILLIS)
            val hours = (difference - DAYS_IN_MILLIS * days) / (HOURS_IN_MILLIS)
            val minutes = (difference - (DAYS_IN_MILLIS * days) - (HOURS_IN_MILLIS * hours)) / (1000 * 60)
            time.add(hours)
            time.add(minutes)
            time
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date difference parse exception: startTime: $startTime endTime: $endTime")
        }
    }

    fun getStartOfDay(day: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = day
        cal[Calendar.HOUR_OF_DAY] = cal.getMinimum(Calendar.HOUR_OF_DAY)
        cal[Calendar.MINUTE] = cal.getMinimum(Calendar.MINUTE)
        cal[Calendar.SECOND] = cal.getMinimum(Calendar.SECOND)
        cal[Calendar.MILLISECOND] = cal.getMinimum(Calendar.MILLISECOND)
        return cal.time
    }

}
