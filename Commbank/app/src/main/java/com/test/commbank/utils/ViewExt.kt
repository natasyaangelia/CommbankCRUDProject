package com.test.commbank.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.test.commbank.R
import com.test.commbank.ui.editprofile.EditProfileFragment
import org.jetbrains.anko.layoutInflater

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Context?.showKeyboard() {
    (this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun Context?.hideKeyboard(view: View?) {
    (this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view?.windowToken,
        0
    )
}

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

const val Toast_Error = -1
const val Toast_Default = 0

@SuppressLint("InflateParams")
fun Context.showToast(
    message: CharSequence,
    typeToast: Int? = Toast_Default,
    isLong: Boolean? = true
) {
    val view = this.layoutInflater.inflate(R.layout.custom_toast, null)
    val txtMessage = view.findViewById<AppCompatTextView>(R.id.txtMessage)
    txtMessage.text = message
    when (typeToast) {
        Toast_Error -> {
            txtMessage.background = ContextCompat.getDrawable(this, R.drawable.bg_toast_error)
        }
        else -> {
            txtMessage.background = ContextCompat.getDrawable(this, R.drawable.bg_toast)
        }
    }

    val toast = Toast(this)
    toast.duration = if (isLong != false) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
    toast.view = view
    toast.show()
}