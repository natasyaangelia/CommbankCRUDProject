package com.test.commbank.ui.browseprofile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.test.commbank.data.model.*
import com.test.commbank.data.pagingsource.BrowseUserPagingSource
import com.test.commbank.data.repository.ApiRepoUserJwt
import com.test.commbank.ui.base.BaseViewModel
import com.test.commbank.utils.ErrorUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.random.Random

class BrowseProfileViewModel(
    private val apiRepoUserJwt: ApiRepoUserJwt,
    application: Application
): BaseViewModel<BrowseProfileNavigator>(application) {

    private val _deleteUser = MutableLiveData<Resource<retrofit2.Response<Unit>>>()
    val deleteUser: MutableLiveData<Resource<retrofit2.Response<Unit>>>
        get() = _deleteUser

    private val _usersData =
        Pager(
            config = PagingConfig(pageSize = 100, maxSize = 500),
            pagingSourceFactory = {BrowseUserPagingSource(apiRepoUserJwt)}).flow
            .cachedIn(viewModelScope)
            .asLiveData()
            .let { it as MutableLiveData<PagingData<BrowseEmployee.Response.Data>> }

    val usersData: LiveData<PagingData<BrowseEmployee.Response.Data>> = _usersData

    fun onViewEvent(browseEmployeeViewEvents: BrowseEmployeeViewEvents) {
        val pagingData = usersData.value ?: return

        when (browseEmployeeViewEvents) {
            is BrowseEmployeeViewEvents.Remove -> {
                pagingData
                    .filter { browseEmployeeViewEvents.browseEmployeeEntity.id != it.id }
                    .let { _usersData.value = it }
            }
        }
    }

    fun deleteUserAsync(userId: Int) {
        _deleteUser.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = apiRepoUserJwt.deleteUsersAsync(userId)
                if (response.code() == 204)
                    _deleteUser.postValue(Resource.success(response))
                else {
                    onErrorDeleteUser(ErrorUtils.getMsgFromError(response))
                }
            } catch (t: Throwable) {
                onErrorDeleteUser(ErrorUtils.getMsgFromError(t), t)
            }
        }
    }

    private fun onErrorDeleteUser(msg: String?, err: Throwable? = null) {
        _deleteUser.postValue(
            Resource.error(
                msg,
                null,
                err
            )
        )
    }
}