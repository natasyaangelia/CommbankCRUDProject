package com.test.commbank.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*

fun <T> Fragment.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { action.invoke(it) })
}

fun <T> LiveData<T>.nullify(): Unit {
    (this as MutableLiveData<T>).value = null
}