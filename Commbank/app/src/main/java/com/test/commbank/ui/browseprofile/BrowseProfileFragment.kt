package com.test.commbank.ui.browseprofile

import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.commbank.R
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.data.model.BrowseEmployeeViewEvents
import com.test.commbank.data.model.Status
import com.test.commbank.databinding.BrowseProfileFragmentBinding
import com.test.commbank.ui.base.BaseFragment
import com.test.commbank.ui.editprofile.EditProfileFragmentArgs
import com.test.commbank.utils.*
import com.test.commbank.utils.navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowseProfileFragment : BaseFragment<BrowseProfileFragmentBinding, BrowseProfileViewModel>(true),
    BrowseProfileNavigator {

    private val controller: NavController? by lazy { view?.findNavController() }
    private val browseProfileViewModel: BrowseProfileViewModel by viewModel()
    private lateinit var binding: BrowseProfileFragmentBinding
    private lateinit var browseProfileAdapter: BrowseProfileAdapter
    private lateinit var deleteData: BrowseEmployee.Response.Data

    override fun onInitialization() {
        super.onInitialization()
        binding = getViewDataBinding()
        browseProfileViewModel.navigator = this
        binding.lifecycleOwner = this
    }

    override fun setLayout(): Int {
        return R.layout.browse_profile_fragment
    }

    override fun getViewModels() = browseProfileViewModel

    override fun onObserveAction() {
        observe(browseProfileViewModel.usersData) { pagingData ->
            browseProfileAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        observe(browseProfileViewModel.deleteUser) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        Loading.showLoading(requireActivity())
                    }
                    Status.SUCCESS -> {
                        Loading.hideLoading()
                        requireContext().showToast(requireContext().getString(R.string.success_delete_user))
                        browseProfileViewModel.onViewEvent(BrowseEmployeeViewEvents.Remove(deleteData))
                    }
                    Status.ERROR -> {
                        Loading.hideLoading()
                        it.message?.let { msg -> requireContext().showToast(msg, Toast_Error,false) }
                    }
                    else -> {
                        Loading.hideLoading()
                    }
                }
            }
        }
    }

    override fun onReadyAction() {
        initRecyclerView()

        binding.swpRefresh.apply {
            setOnRefreshListener {
                browseProfileAdapter.refresh()
                isRefreshing = false
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvBrowseResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            browseProfileAdapter = BrowseProfileAdapter(this@BrowseProfileFragment)
            adapter = browseProfileAdapter
        }
    }

    override fun deleteUser(data: BrowseEmployee.Response.Data) {
        deleteData = data
        browseProfileViewModel.deleteUserAsync(data.id)
    }

    override fun updateUser(data: BrowseEmployee.Response.Data) {
        mainNavController().goToUpdateUser(
            EditProfileFragmentArgs(
                data
            ).toBundle()
        )
    }

    override fun onResume() {
        super.onResume()
        showStaticToolbar(getString(R.string.user_list))
    }

    override fun onPause() {
        super.onPause()
        invalidStaticToolbar()
    }
}