package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts: List<String>? = fullName?.trim()?.split(" ")
        val firstName = parts?.getOrNull(0)?.ifBlank { null }
        val lastName = parts?.getOrNull(1)?.ifBlank { null }

        return  firstName to lastName

    }

    fun transliteration(payload: String, divider: String = " "): String {
        var translatePayload: String = ""
//создаем асоциативный массив ключ-значение
        val dictionary = mapOf(
            "а" to "a",
            "б" to "b",
            "в" to "v",
            "г" to "g",
            "д" to "d",
            "е" to "e",
            "ё" to "e",
            "ж" to "zh",
            "з" to "z",
            "и" to "i",
            "й" to "i",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "х" to "h",
            "ц" to "c",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "sh'",
            "ъ" to "",
            "ы" to "i",
            "ь" to "",
            "э" to "e",
            "ю" to "yu",
            "я" to "ya",
            " " to divider

        )
        //перебираем строку как массив символов
        payload.forEach { letter ->

           if (letter.toString().toLowerCase() in dictionary.keys) {
               var translateLetter = dictionary.getValue(letter.toLowerCase().toString())

//Добавляем большие букву в первый транстилитированный символ, если нужно
            if (translateLetter == "") translateLetter = ""  //Для заглавного твердого знака
            else if(letter.isUpperCase()&& (translateLetter.length ==1)) translateLetter = translateLetter[0].toString().toUpperCase()
            else if (letter.isUpperCase()) translateLetter = translateLetter[0].toString().toUpperCase() + translateLetter.substring(1)

            translatePayload += translateLetter
           }
            else translatePayload += letter

        }


        return translatePayload

    }
    fun toInitials(firstname: String?, lastname: String? ): String? {
        var trim_fistname: String?
        var trim_lastname: String?


        trim_fistname = firstname?.trim()
        trim_lastname =  lastname?.trim()

       //println ("lastname: \"$lastname\" \n firsname: \"$firstname\" ${firstname?.length}")
        val firstname_firsletter: String?
        val lastname_firsletter: String?
        //  firstname?.first()


        if (trim_fistname != "")   firstname_firsletter = trim_fistname?.trim()?.first()?.toUpperCase().toString() else firstname_firsletter = ""
        if (trim_lastname != "") lastname_firsletter = trim_lastname?.first()?.toUpperCase().toString() else lastname_firsletter = ""



        val inintials = when {

            (firstname == null || firstname.trim() == "") && (lastname == null || lastname.trim() == "")  -> null

            firstname?.trim() == null -> lastname_firsletter
            lastname?.trim()  == null -> firstname_firsletter
            else -> firstname_firsletter+lastname_firsletter



        }

        return inintials
    }

}