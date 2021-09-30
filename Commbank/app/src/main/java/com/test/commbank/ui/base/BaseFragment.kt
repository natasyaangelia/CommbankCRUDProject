package com.test.commbank.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.test.commbank.BR
import com.test.commbank.utils.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<out Any>>(private val toolbar: Boolean) :
    Fragment(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var mainView: MainView
    private lateinit var dataBinding: T
    private lateinit var rootView: View
    private val baseViewModel by lazy { getViewModels() }

    @LayoutRes
    abstract fun setLayout(): Int

    abstract fun getViewModels(): V

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    /**
     * Only for in the fragment menu
     *
     * Each argument key must be removed
     */
    open fun onClearArguments() = Unit

    open fun onFragmentDestroyed() = Unit

    open fun getViewDataBinding() = dataBinding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfiguration()
        setHasOptionsMenu(true)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, setLayout(), container, false)
        rootView = dataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.setVariable(BR.data, baseViewModel)
        onInitialization()
        dataBinding.executePendingBindings()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resetToolbar()
        mainView.recycleToolbar()
        activity.hideKeyboard(view)
        baseViewModel.toolbarState.value = toolbar
        baseViewModel.toolbarState.observe(viewLifecycleOwner) {
            if (it) {
                mainView.toolbarState(true)
            } else {
                mainView.toolbarState(false)
            }
        }
        onReadyAction()
        onObserveAction()
    }

    override fun onPause() {
        super.onPause()
        onClearArguments()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        resetToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onFragmentDestroyed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainView = activity as MainView
    }

    fun showStaticToolbar(title: String) {
        mainView.staticToolbar(title)
    }

    fun invalidStaticToolbar() {
        mainView.invalidToolbar()
    }

    fun setActionToolbar(toolbar: Toolbar) {
        mainView.setActionToolbar(toolbar)
    }

    fun disallowScroll() {
        mainView.invalidScroll()
    }

    fun allowScroll() {
        mainView.backToScrolling()
    }

    fun defaultToolbar(title: String, invalidNav: Boolean = false) {
        mainView.initDefaultToolbar(title, invalidNav)
    }

    fun customToolbar(title: String, menu: Int? = null, navIcon: Int? = null) {
        mainView.initCustomToolbar(title, menu, navIcon)
    }

    private fun resetToolbar() {
        mainView.resetToolbar()
    }

    fun getToolbarInstance(): MaterialToolbar = mainView.getToolbar()

    fun scrollviewEnabled(status: Boolean = true) = mainView.setEnableScrollview(status)

    private fun initConfiguration() {
        val res = requireContext().resources
        val resConfig = res.configuration
        val config = Configuration(resConfig)
        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)
    }

    fun changeOrientation(orientation: Int) {
        activity?.requestedOrientation = orientation
    }
}