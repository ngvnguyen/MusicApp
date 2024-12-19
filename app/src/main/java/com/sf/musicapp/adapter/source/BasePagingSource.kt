package com.sf.musicapp.adapter.source

import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSource<T:Any>(
    private val getData:suspend (Int)-> List<T>
): PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            return anchorPage?.prevKey?.inc()?: anchorPage?.nextKey?.dec()
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try{
            val nextPage = params.key?:0
            val data = getData(nextPage)

            LoadResult.Page(
                data,
                if (nextPage>0) nextPage-1 else null,
                nextPage+1
            )
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

}