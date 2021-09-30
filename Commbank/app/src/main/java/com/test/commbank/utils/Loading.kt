package com.test.commbank.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.appcompat.app.AlertDialog
import com.test.commbank.R
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.windowManager

object Loading {
    private var loading: AlertDialog? = null

    fun showLoading(context: Context) {
        if (loading == null) {
            val view = context.layoutInflater.inflate(R.layout.custom_loading, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            builder.setCancelable(false)
            loading = builder.create()
        }

        if (loading?.isShowing == false) {
            loading?.show()
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val dialogWindowWidth = (displayWidth * 0.15f).toInt()
            loading?.window?.setBackgroundDrawableResource(R.drawable.bg_transparant)
            loading?.window?.setLayout(dialogWindowWidth, dialogWindowWidth)
        }
    }

    fun hideLoading() {
        if (loading?.isShowing == true) {
            loading?.dismiss()
            loading = null
        }
    }
}