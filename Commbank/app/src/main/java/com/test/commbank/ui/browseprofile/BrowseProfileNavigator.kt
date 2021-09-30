package com.test.commbank.ui.browseprofile

import com.test.commbank.data.model.BrowseEmployee

interface BrowseProfileNavigator {
    fun deleteUser(data: BrowseEmployee.Response.Data)
    fun updateUser(data: BrowseEmployee.Response.Data)
}