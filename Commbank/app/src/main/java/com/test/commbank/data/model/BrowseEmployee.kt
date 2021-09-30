package com.test.commbank.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class BrowseEmployeeViewEvents {
    data class Edit(val browseEmployeeEntity: BrowseEmployee.Response.Data) : BrowseEmployeeViewEvents()
    data class Remove(val browseEmployeeEntity: BrowseEmployee.Response.Data) : BrowseEmployeeViewEvents()
}

object BrowseEmployee {

    data class Response(
        val meta: Meta,
        val `data`: List<Data> = emptyList()
    ) {
        class Meta(
            val pagination: Pagination
        ){
            class Pagination(
                val total: Int,
                val pages: Int,
                val page: Int,
                val limit: Int,
                val links: Link
            ){
                class Link(
                    val previous: String?,
                    val current: String?,
                    val next: String?
                )
            }
        }

        @Parcelize
        class Data(
            val id: Int,
            val name: String,
            val email: String,
            val gender: String,
            val status: String
        ): Parcelable
    }
}