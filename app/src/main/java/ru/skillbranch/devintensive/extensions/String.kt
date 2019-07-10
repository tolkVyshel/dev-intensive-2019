package ru.skillbranch.devintensive.extensions

fun String.truncate (limit: Int = 16): String{
    return if (this.trim().length > (limit)) {this.trim().substring(0, limit).trim() + "..."} else this.trim()
}





fun String.stripHtml (): String{

    var clearString: String = ""
    clearString = this.replace(Regex("<.*?>"), "").replace(Regex("(&[^-аЯ| ]*?;)"), "").replace(Regex(" {2,}"), " ")




    return clearString
}