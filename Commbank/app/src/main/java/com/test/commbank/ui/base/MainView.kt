package com.test.commbank.ui.base

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar

interface MainView {
    fun recycleToolbar()
    fun staticToolbar(title: String)
    fun invalidToolbar()
    fun invalidScroll()
    fun backToScrolling()
    fun initDefaultToolbar(title: String, invalidNav: Boolean)
    fun initCustomToolbar(title: String, menu: Int?, navIcon: Int?)
    fun toolbarState(state: Boolean)
    fun getToolbar(): MaterialToolbar
    fun setEnableScrollview(status: Boolean)
    fun setActionToolbar(toolbar: Toolbar)
    fun resetToolbar()
}