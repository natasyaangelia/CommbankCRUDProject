package com.test.commbank.utils

import android.util.Patterns

fun String.isEmailValid(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()