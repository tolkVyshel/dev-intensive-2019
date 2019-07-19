package ru.skillbranch.devintensive


import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender
import android.R.attr.bottom
import android.graphics.Rect
import ru.skillbranch.devintensive.extensions.isKeyboardClosed


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    var intErrorAnswer: Int = 0 // счетчик неверных ответов

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("M_MainActivity", "onCreate")

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        messageEt.imeOptions = EditorInfo.IME_ACTION_DONE;

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        intErrorAnswer = savedInstanceState?.getInt("intErrorAnswer", 0)?: 0
        Log.d("M_MainActivity", "status = $status    question = $question")
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val(r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        messageEt.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onClick(sendBtn)
                this.hideKeyboard()
                true
            } else {
                false
            }
        }
        //  add to onCreate method for Activity.isKeyboardOpen Activity.isKeyboardClosed
        val rectgle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(rectgle)
        var sheight = rectgle.bottom
//

       /*Log.d("M_MainActivity", "${this.isKeyboardClosed(sheight, window)}")*/

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            val (phase, color, intError: Int) = benderObj.listenAnswer(messageEt.text.toString(), intErrorAnswer) //
            intErrorAnswer = intError
            Log.d("M_MainActivity", "${messageEt.text.toString()}") //.toLowerCase()
            messageEt.setText("")
            val(r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phase

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        outState?.putInt("intErrorAnswer", intErrorAnswer)
        Log.d("M_MainActivity", "instance is saved")
    }



}