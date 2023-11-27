package com.millenialzdev.storyapp.view.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.millenialzdev.storyapp.remote.response.ListStoryItem
import com.millenialzdev.storyapp.remote.response.StoryResponse
import com.millenialzdev.storyapp.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoryPagingSource(
    private val token: String
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = fetchDataAsynchronously(token, page, params.loadSize)
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun fetchDataAsynchronously(token: String, position: Int, loadSize: Int): List<ListStoryItem> {
        return suspendCoroutine { continuation ->
            ApiConfig.getApiService(token).getStories(position, loadSize).enqueue(object :
                Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            val storyList = responseData.listStory
                            continuation.resume(storyList)
                        }
                    } else {
                        continuation.resumeWithException(Exception("API request failed"))
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}