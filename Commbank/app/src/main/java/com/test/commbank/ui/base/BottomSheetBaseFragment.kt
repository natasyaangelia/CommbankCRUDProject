package com.test.commbank.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.commbank.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BottomSheetBaseFragment<T : ViewDataBinding, V : BaseViewModel<out Any>> :
    BottomSheetDialogFragment(),
    CoroutineScope {

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

    abstract fun onObserveAction()

    open fun getViewDataBinding() = dataBinding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        mainView.recycleToolbar()
        onReadyAction()
        onObserveAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainView = activity as MainView
    }
}