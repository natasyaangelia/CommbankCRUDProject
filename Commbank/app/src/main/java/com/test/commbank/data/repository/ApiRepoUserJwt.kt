package com.test.commbank.data.repository

import com.test.commbank.data.model.AddEmployee
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.data.model.DeleteEmployee
import com.test.commbank.data.model.UpdateEmployee
import com.test.commbank.data.source.endpoint.ApiServiceUserBearer

class ApiRepoUserJwt(private val apiService: ApiServiceUserBearer) {

    suspend fun getUsersAsync(page: Int): BrowseEmployee.Response =
        apiService.getUsersAsync(page).await()

    suspend fun addUserAsync(user: AddEmployee.Request): AddEmployee.Response =
        apiService.addUserAsync(user).await()

    suspend fun updateUsersAsync(userId: Int, user: UpdateEmployee.Request): UpdateEmployee.Response =
        apiService.updateUserAsync(userId, user).await()

    suspend fun deleteUsersAsync(userId: Int): retrofit2.Response<Unit> =
        apiService.deleteUserAsync(userId).await()
}