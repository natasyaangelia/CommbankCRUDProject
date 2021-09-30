package com.test.commbank.data.pagingsource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.data.repository.ApiRepoUserJwt

class BrowseUserPagingSource(
    private val apiRepoUserJwt: ApiRepoUserJwt
) : PagingSource<Int, BrowseEmployee.Response.Data>() {

    override fun getRefreshKey(state: PagingState<Int, BrowseEmployee.Response.Data>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BrowseEmployee.Response.Data> {
        return try {
            val nextPage: Int = params.key ?: FIRST_PAGE_INDEX
            val response = apiRepoUserJwt.getUsersAsync(nextPage)
            var nextPageNumber: Int? = null

            if (response.meta.pagination.links.next != null) {
                val uri = Uri.parse(response.meta.pagination.links.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

}