package com.test.commbank.utils.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.test.commbank.R

fun Fragment.mainNavController() = requireActivity().findNavController(R.id.nav_host_fragment_main)

fun NavController.go(
    @IdRes destinationId: Int,
    bundle: Bundle? = null,
    @IdRes popUpToIdRes: Int? = null,
    isInclusive: Boolean = false,
    isLaunchSingleTop: Boolean = false
) {
    navigate(destinationId, bundle, navOptions {
        popUpToIdRes?.let {
            popUpTo(it) {
                inclusive = isInclusive
            }
        }
        launchSingleTop = isLaunchSingleTop
        anim {
            enter = R.anim.fade_in
            exit = R.anim.fade_out
            popEnter = R.anim.fade_in
            popExit = R.anim.fade_out
        }
    })
}

fun NavController.goToUpdateUser(
    bundle: Bundle? = null,
    @IdRes popUpToIdRes: Int? = null,
    isInclusive: Boolean = false,
    isLaunchSingleTop: Boolean = false
) {
    go(
        R.id.action_to_editFragment,
        bundle,
        popUpToIdRes,
        isInclusive,
        isLaunchSingleTop
    )
}