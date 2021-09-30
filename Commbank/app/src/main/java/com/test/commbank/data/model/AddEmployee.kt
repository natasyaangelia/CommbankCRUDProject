package com.test.commbank.data.model

object AddEmployee {

    data class Request(
        val name: String,
        val gender: String,
        val email: String,
        val status: String
    )

    data class Response(
        val meta: Meta?,
        val `data`: Data
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

        class Data(
            val id: Int,
            val name: String,
            val email: String,
            val gender: String,
            val status: String
        )
    }

}