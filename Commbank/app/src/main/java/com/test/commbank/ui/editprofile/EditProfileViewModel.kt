package com.test.commbank.ui.editprofile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.commbank.data.model.AddEmployee
import com.test.commbank.data.model.Resource
import com.test.commbank.data.model.UpdateEmployee
import com.test.commbank.data.repository.ApiRepoUserJwt
import com.test.commbank.ui.base.BaseViewModel
import com.test.commbank.utils.ErrorUtils
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val apiRepoUserJwt: ApiRepoUserJwt,
    application: Application
): BaseViewModel<EditProfileNavigator>(application) {

    var validInput = MutableLiveData(false)
    var validName = MutableLiveData(false)
    var validEmail = MutableLiveData(false)
    var validGender = MutableLiveData(false)
    var isEdit = MutableLiveData(false)

    private val _addUser = MutableLiveData<Resource<AddEmployee.Response>>()
    val addUser: MutableLiveData<Resource<AddEmployee.Response>>
        get() = _addUser

    private val _updateUser = MutableLiveData<Resource<UpdateEmployee.Response>>()
    val updateUser: MutableLiveData<Resource<UpdateEmployee.Response>>
        get() = _updateUser

    fun addUserAsync(request: AddEmployee.Request) {
        _addUser.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = apiRepoUserJwt.addUserAsync(request)
                _addUser.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _addUser.postValue(
                    Resource.error(
                        ErrorUtils.getMsgFromErrorAddUser(t),
                        null,
                        t
                    )
                )
            }
        }
    }

    fun updateUserAsync(userId: Int, request: UpdateEmployee.Request) {
        _updateUser.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = apiRepoUserJwt.updateUsersAsync(userId, request)
                _updateUser.postValue(Resource.success(response))
            } catch (t: Throwable) {
                _updateUser.postValue(
                    Resource.error(
                        ErrorUtils.getMsgFromError(t),
                        null,
                        t
                    )
                )
            }
        }
    }
}