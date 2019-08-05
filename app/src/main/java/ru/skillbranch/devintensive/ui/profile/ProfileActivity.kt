package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile_constrait.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.getInitAvatar
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.utils.Utils.toInitials
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        //TODO set custom theme this before super and set ContentView
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_constrait)
        initViews(savedInstanceState)
        initViewModel()


//вешаю слушатель на едиттекст и проверяю вводимый текст регулярками
        et_repository.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(inputText: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(checkRepositoryInput(inputText.toString()))  wr_repository.error = null

                else  wr_repository.error  = "Невалидный адрес репозитория"



            }
        })


    }

    private fun checkRepositoryInput(repo: String): Boolean {

        Log.d("M_ProfileActivity", "github repo: $repo")
        var reg: Regex = Regex("""(https://)?(www\.)?github.com/[a-zA-Z0-9-\-]+(/)?""")
        if (repo == "" || reg.matches(repo)) {
            Log.d("M_ProfileActivity", "Репозиторий Валидный по Регулярке")


            listOfException.forEach {
                var reg2 = Regex(""".*/$it[/]?$.*""")
                if (reg2.matches(repo)) {
                    Log.d("M_ProfileActivity", "Репозиторий содержит исключения")

                    return false
                }
            }

            return true
        }
        return false
        /*   else
        {
            Log.d("M_ProfileActivity", "matches false")
            wr_repository.error  = "Невалидный адрес репозитория"
        }*/


    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
      //  viewModel.getProfileData().observe(this, Observer { updateDrawable(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
       // updateDrawable(viewModel.getProfileData().value)
    }

    private fun updateAvatar(profile: Profile) {
        toInitials(profile.firstName, profile.lastName)?.let { iv_avatar.setImageDrawable(getInitAvatar(it)) }
            ?: iv_avatar.setImageResource(R.drawable.avatar_default)
    }

    private fun updateDrawable(profile: Profile?){

        val initials = Utils.toInitials(profile?.firstName, profile?.lastName)
        val drawable = if (initials==null) {
            resources.getDrawable(R.drawable.ic_avatar, theme)
        } else {
            val color = TypedValue()
            theme.resolveAttribute(R.attr.colorAccent, color, true)
            ColorDrawable(color.data)
        }
        iv_avatar.setImageDrawable(drawable)
       // iv_avatar.text = initials

    }

    private fun updateUI(profile: Profile) {
       // updateDrawable(profile)

        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
        updateAvatar(profile)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect)

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = isEditMode.not()
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            setOf("firstName", "lastName", "about", "repository" ).contains(it.key)
        }

        info.forEach {
            val v = it.value as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }
        iv_avatar

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit){
            val filter: ColorFilter? = if (isEdit){
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme), PorterDuff.Mode.SRC_IN)
            }
            else null

            val icon =
                if (isEdit)
                    resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
                else resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo(){
       var  saveRepo: String
        Log.d("M_ProfileActivity", "wr_repository.error: ${wr_repository.error}")
        if (wr_repository.error == null) saveRepo = et_repository.text.toString()
        else {
            saveRepo = ""
            et_repository.text!!.clear() //очищаю edittext
            wr_repository.error = null //сбрасываю ошибку


        }
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository =   saveRepo
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }
    val listOfException = listOf<String>(
        "enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    )
}
