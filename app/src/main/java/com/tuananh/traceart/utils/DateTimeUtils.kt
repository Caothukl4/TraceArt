package com.tuananh.traceart.utils

import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    /**
     * Định dạng timestamp theo dd/MM/yyyy HH:mm (theo Locale thiết bị).
     */
    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    /**
     * Định dạng timestamp theo hệ thống (ngày + giờ ngắn gọn theo Locale).
     */
    fun formatTimestampSystem(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        return DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT,
            Locale.getDefault()
        ).format(date)
    }

    /**
     * Định dạng từ millis (Long) sang dd/MM/yyyy HH:mm theo Locale.
     */
    fun formatMillis(millis: Long?): String {
        if(millis==null){
            return ""
        }
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    /**
     * Định dạng từ millis (Long) sang dạng hệ thống theo Locale.
     */
    fun formatMillisSystem(millis: Long?): String {
        if(millis==null){
            return ""
        }
        val date = Date(millis)
        return DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT,
            Locale.getDefault()
        ).format(date)
    }
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        return if (hours > 0)
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        else
            String.format("%02d:%02d", minutes, seconds)
    }


    fun isSameDay(ts1: Long, ts2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = ts1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = ts2 }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}