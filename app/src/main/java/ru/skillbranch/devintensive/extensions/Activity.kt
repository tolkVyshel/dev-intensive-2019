package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.graphics.Rect
import android.util.TypedValue
import kotlin.math.roundToInt


fun Activity.hideKeyboard(){
  //  var hasFocus = this.currentFocus

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.findViewById<View>(android.R.id.content).windowToken, 0)
    }



class OnFocusLostListener: View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}


/*fun Activity.isKeyboardClosed2(sheight:Int, window: Window): Boolean {
    val rectgle = Rect()
    //val window = getWindow()
    window.getDecorView().getWindowVisibleDisplayFrame(rectgle)
    val curheight = rectgle.bottom

    return if (curheight == sheight) {
        true
    } else {
        false
    }}*/








fun Activity.isKeyboardOpen(): Boolean {
    val visiblity = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(visiblity)
    val diff = rootView.height - visiblity.height()
    val margin = this.convertDpToPx(50F).roundToInt()
    return diff > margin
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}


/*
fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}*/
