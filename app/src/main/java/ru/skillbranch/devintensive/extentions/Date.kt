package ru.skillbranch.devintensive.extentions


import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateformat = SimpleDateFormat(pattern, Locale("ru"))
    return  dateformat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

  time +=  when (units) {
      TimeUnits.SECOND -> value * SECOND
      TimeUnits.MINUTE -> value * MINUTE
      TimeUnits.HOUR -> value * HOUR
      TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return  this


}
public fun Date.humanizeDiff(date: Date = Date()): String {
    TODO()
}


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY

}