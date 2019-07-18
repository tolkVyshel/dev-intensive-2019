package ru.skillbranch.devintensive.models

import android.util.Log

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME){

    fun askQuestion():String = when(question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer:String, intErrorAnswer: Int):Triple<String, Triple<Int, Int, Int>, Int>{
        var returnIntErrorAnswer = intErrorAnswer
        lateinit var   tri :Triple<String, Triple<Int, Int, Int>, Int> //= Triple("", status.color, returnIntErrorAnswer)

            if (question.answer.contains(answer)) {
            question = question.nextQuestion()
                Log.d("M_Bender", "returnIntErrorAnswer: $returnIntErrorAnswer")
                tri=  Triple("Отлично - ты справился\n${question.question}", second = status.color,  third = returnIntErrorAnswer)

        }
      else {
            returnIntErrorAnswer ++
            if (returnIntErrorAnswer > 3) {
                Log.d("M_Bender", "returnIntErrorAnswer: $returnIntErrorAnswer")
                returnIntErrorAnswer = 0
                resetStatus()

                tri=    Triple("Это неправильный ответ. Давай все по новой\n${question.question}", second = status.color, third = returnIntErrorAnswer)
            }
            else{
            status = status.nextStatus()
                Log.d("M_Bender", "returnIntErrorAnswer: $returnIntErrorAnswer")

                tri=    Triple("Это неправильный ответ\n${question.question}", second = status.color, third = returnIntErrorAnswer)}
        }



        return tri
    }

     fun resetStatus() {
        status = Status.NORMAL
        question = Question.NAME
    }

    enum class Status(val color: Triple<Int, Int, Int>){
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status{
            return if (this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            }
            else{
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>){
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "iron", "wood", "metal")){
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion():Question
    }
}