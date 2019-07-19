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


        if(!question.validationAnswer(answer)){
            val error_msg = when (question) {
                Question.NAME -> "Имя должно начинаться с заглавной буквы\n${question.question}"

                Question.PROFESSION -> "Профессия должна начинаться со строчной буквы\n${question.question}"
                Question.MATERIAL -> "Материал не должен содержать цифр\n${question.question}"
                Question.BDAY -> "Год моего рождения должен содержать только цифры\n${question.question}"
                Question.SERIAL -> "Серийный номер содержит только цифры, и их 7\n${question.question}"
                Question.IDLE ->  "На этом все, вопросов больше нет"
            }
                Log.d("M_Bender", "eroor validation: $error_msg")
            return Triple(error_msg, status.color, returnIntErrorAnswer)

        }


        Log.d("M_Bender", "$question - $answer")


            if (question.answer.contains(answer.toLowerCase())) {
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
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")
                val check = userAnswer.trim().firstOrNull()?.isUpperCase()

                return !(check == false ||  check == null)
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun nextQuestion(): Question = MATERIAL
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")
                val check = userAnswer.trim().firstOrNull()?.isLowerCase()

                return !(check == false ||  check == null)
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "iron", "wood", "metal")){
            override fun nextQuestion(): Question = BDAY
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")

                return !userAnswer.trim().contains(regex = Regex("[0-9]"))
            }
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")
                val regex = Regex(pattern = """\d+""")
                regex.matches(userAnswer.trim())

                return regex.matches(userAnswer.trim())

            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")
                val regex = Regex(pattern = """\d{7}""")
                regex.matches(userAnswer.trim())

                return regex.matches(userAnswer.trim())

            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun nextQuestion(): Question = IDLE
            override fun validationAnswer(userAnswer:String): Boolean {
                Log.d("M_Bender", "validationAnswer: $answer")

                return false
            }
        };

        abstract fun nextQuestion():Question
        abstract fun validationAnswer(userAnswer: String):Boolean
    }
}