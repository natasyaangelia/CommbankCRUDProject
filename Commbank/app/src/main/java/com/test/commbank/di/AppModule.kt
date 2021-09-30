package com.test.commbank.di

import com.test.commbank.data.repository.ApiRepoUserJwt
import com.test.commbank.ui.browseprofile.BrowseProfileViewModel
import com.test.commbank.ui.editprofile.EditProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BrowseProfileViewModel(get(), androidApplication()) }
    viewModel { EditProfileViewModel(get(), androidApplication()) }
}

val apiRepositoryModule = module {
    single { ApiRepoUserJwt(get()) }
}