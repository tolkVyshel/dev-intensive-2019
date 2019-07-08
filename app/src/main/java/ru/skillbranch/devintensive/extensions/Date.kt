package ru.skillbranch.devintensive.extensions


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
fun Date.humanizeDiff(date: Date = Date()): String {


    var humanizeInterval: String

    val interval = this.time - date.time
    println("time now ${Date().time}")
    println("time old ${date.time}")
    println("interval $interval")

    humanizeInterval =  when (interval) {
        in 0 .. 1 -> "только что"
        in 1 until 45 -> "несколько секунд назад"
        in 45 until 75 -> "минуту назад"
        in 75 until (45*60) -> "(45*60) минут назад"


        else -> "хз"
    }



    return humanizeInterval
}


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY

}