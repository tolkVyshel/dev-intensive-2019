package ru.skillbranch.devintensive.extensions


import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

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

    val interval = (abs(this.time - date.time)/1000).toInt() //получаем интервал времени в секундах

    val isNegative = this.time < date.time


    fun human_interval(human_int: String, isNegative:Boolean):String

    {
        val humanIntPre: String
        humanIntPre = if (isNegative)  "$human_int назад" else "через $human_int"

        return humanIntPre.trim()


    }

    humanizeInterval =  when (interval) {

        in 0 .. 1 -> "только что"
        in  1 .. 45 -> if (isNegative) "несколько секунд назад" else "через несколько секунд"
        in 45 .. 75 -> if (isNegative) "минуту назад" else "через минуту"
        in 75 ..  (45*60) -> "${human_interval(TimeUnits.MINUTE.plural(interval/60), isNegative )}"
        in (45*60) ..(75*60) -> if (isNegative) "час назад" else "через час"
        in (75*60) ..(22*60*60) -> "${human_interval(TimeUnits.HOUR.plural(interval/60/60), isNegative )}"
        in (22*60*60) ..(26*60*60) -> if (isNegative) "день назад" else "через день"
        in (26*60*60) ..(360*60*60*24) -> "${human_interval(TimeUnits.DAY.plural(interval/60/60/24), isNegative )}"

        else -> if (isNegative) "более года назад" else "более чем через год"
    }



    return humanizeInterval
}




enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;
  //  unit: TimeUnits,
 fun plural( value: Int): String{
     val pluralForm: String
      var remainder = value%100 //для числе выше сотки
     pluralForm = when (remainder) {
         1 -> "$value ${Plurals.ONE.get(this)}"
         in 2 .. 4 -> "$value ${Plurals.FEW.get(this)}"
         in 5 .. 19 -> "$value ${Plurals.MANY.get(this)}"
         else  -> {
             remainder %= 10

             var humanMinutesInterval: String = when (remainder)  {
                 1 -> "$value ${Plurals.ONE.get(this)}"
                 in 2..4 -> "$value ${Plurals.FEW.get(this)}"
                 in 5..9, 0 -> "$value ${Plurals.MANY.get(this)}"
                 else -> "не учтенный интервал"



             }
             return humanMinutesInterval

         }



     }

       return pluralForm
    }

  enum class Plurals(private val second: String,private val minute: String, private val hour: String, private val day: String){
      ONE("секунду", "минуту", "час", "день"),
      FEW("секунды", "минуты", "часа", "дня"),
      MANY("секунд","минут", "часов", "дней");

      fun get(unit: TimeUnits): String {
          return when(unit){
              SECOND -> second
              MINUTE -> minute
              HOUR -> hour
              DAY -> day

          }
      }
  }
}
fun humanMinutes(interval: Long, isNegative: Boolean = false): String {
    //минуту назад, через минуту
    //2 минуты назад, через 2 минуты
    var minutesInterval = (interval / 60).toInt()
    val remainder = minutesInterval%10

    var humanMinutesInterval: String = when  {
        minutesInterval == 1 -> "$minutesInterval минуту"
        minutesInterval in 2..4 -> "$minutesInterval минуты"
        minutesInterval in 5..19 -> "$minutesInterval минут"
        remainder == 1 -> "$minutesInterval минуту"
        minutesInterval in 2..4 -> "$minutesInterval минуты"
        minutesInterval in 5..9 || minutesInterval == 0 -> "$minutesInterval минут"
        else -> "не учтенный интервал"



    }


    return            if (!isNegative) "через $humanMinutesInterval"
    else "$humanMinutesInterval назад"
}


