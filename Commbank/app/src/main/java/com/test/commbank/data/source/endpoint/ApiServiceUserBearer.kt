package com.test.commbank.data.source.endpoint

import com.test.commbank.data.model.AddEmployee
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.data.model.DeleteEmployee
import com.test.commbank.data.model.UpdateEmployee
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ApiServiceUserBearer {

    @GET("users")
    fun getUsersAsync(
        @Query("page") page: Int
    ): Deferred<BrowseEmployee.Response>

    @POST("users")
    fun addUserAsync(
        @Body request: AddEmployee.Request
    ): Deferred<AddEmployee.Response>

    @DELETE("users/{userId}")
    fun deleteUserAsync(
        @Path("userId") userId: Int
    ): Deferred<retrofit2.Response<Unit>>

    @PUT("users/{userId}")
    fun updateUserAsync(
        @Path("userId") userId: Int,
        @Body request: UpdateEmployee.Request
    ): Deferred<UpdateEmployee.Response>
}