package com.test.commbank.customview

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.test.commbank.R
import com.test.commbank.databinding.CustomFormSingleBinding
import com.test.commbank.utils.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class FormSingle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr) {

    private var rootViewForm: View
    private var et: AppCompatEditText
    private var tvError: AppCompatTextView
    private var etTitle: AppCompatTextView
    private var etSecondHint: AppCompatTextView
    private var form: ConstraintLayout

    init {
        rootViewForm = LayoutInflater.from(context).inflate(R.layout.custom_form_single, this, true)
        
        et = rootViewForm.findViewById(R.id.et)
        tvError = rootViewForm.findViewById(R.id.et_error)
        etTitle = rootViewForm.findViewById(R.id.et_title)
        etSecondHint = rootViewForm.findViewById(R.id.et_second_hint)
        form = rootViewForm.findViewById(R.id.form)
        
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.FormSingle, 0, 0)
            val title = styledAttributes.getString(R.styleable.FormSingle_title)
            val hint = styledAttributes.getString(R.styleable.FormSingle_hint)
            val secondHint = styledAttributes.getString(R.styleable.FormSingle_secondHint)
            val disable = styledAttributes.getBoolean(R.styleable.FormSingle_disable, false)
            val titleCaps = styledAttributes.getBoolean(R.styleable.FormSingle_titleCaps, false)
            val errorText = styledAttributes.getString(R.styleable.FormSingle_errorText)
            val error = styledAttributes.getBoolean(R.styleable.FormSingle_error, false)
            val secondHintVisibility = styledAttributes.getBoolean(R.styleable.FormSingle_secondHintVisibility, false)
            val type = styledAttributes.getInt(R.styleable.FormSingle_typeText, 0)
            val maxLength = styledAttributes.getInt(R.styleable.FormSingle_maxLength, 0)
            setTypeEditText(type)
            setMaxLength(maxLength)
            setHint(hint)
            setTitle(title)
            setErrorText(errorText)
            setTitleCaps(titleCaps)
            setSecondHint(secondHint)
            if (type != 9) setError(error)
            secondHintVisibility(secondHintVisibility)
            setDisabled(disable)
            styledAttributes.recycle()
        }
    }

    private fun setSecondHint(secondHint: String?) {
        if (secondHint != null) {
            etSecondHint.text = secondHint
        }
    }

    fun setDisabled(disable: Boolean) {
        if (disable) {
            et.isEnabled = false
            et.background = ContextCompat.getDrawable(context, R.drawable.bg_input_disable)
        } else {
            et.isEnabled = true
            et.background = ContextCompat.getDrawable(context, R.drawable.bg_input)
        }
    }

    fun setClicked() {
        et.isClickable = true
        et.isFocusable = false
        et.inputType = InputType.TYPE_NULL
    }

    private fun setTitleCaps(titleCaps: Boolean) {
        if (titleCaps) {
            etTitle.isAllCaps = titleCaps
        }
    }

    private fun setTypeEditText(type: Int?) {
        when (type) {
            1 -> et.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            else -> et.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun setTitle(title: String?) {
        if (title != null) {
            etTitle.text = title
        }
    }

    fun setError(error: Boolean, errorRing: Boolean = false) {
        if (error) {
            tvError.visibility = View.VISIBLE
            etSecondHint.gone()
            if (!errorRing) {
                et.background = ContextCompat.getDrawable(context, R.drawable.bg_input_error)
            }
        } else {
            tvError.visibility = View.GONE
            et.background = ContextCompat.getDrawable(context, R.drawable.bg_input)
        }
    }

    fun secondHintVisibility(isVisible: Boolean) {
        if (isVisible) {
            etSecondHint.visible()
            tvError.gone()
        } else {
            etSecondHint.gone()
        }
    }

    private fun setMaxLength(length: Int) {
        if (length != 0) {
            val fArray = arrayOfNulls<InputFilter>(1)
            fArray[0] = InputFilter.LengthFilter(length)
            et.filters = fArray
        }
    }

    fun setErrorText(text: String?) {
        if (text != null) {
            tvError.text = text
        }
    }

    fun setClearText() {
        et.setText("")
        setError(false)
    }

    private fun setHint(hint: String?) {
        if (hint != null) {
            et.hint = hint
        }
    }

    var textValue: String
        get() = et.text.toString()
        set(value) {
            et.setText(value)
        }

    val editTextInstance: AppCompatEditText = et
    val formInstance: ConstraintLayout = form
}