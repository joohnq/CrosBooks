package com.joohnq.crosbooks.model.network.categories

import android.util.Log
import com.joohnq.crosbooks.constants.AppConstants
import com.joohnq.crosbooks.model.entities.Category
import com.joohnq.crosbooks.model.network.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface CategoriesRepository {
    fun getCategories(): Flow<List<Category>>
}

class CategoriesRepositoryImpl(
    private val categoriesService: CategoriesService,
    private val ioDispatcher: CoroutineDispatcher
) : CategoriesRepository {
    override fun getCategories(
    ): Flow<List<Category>> {
        return flow {
            try {
                val res = categoriesService.getCategories()
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
                val categories = res.body() ?: throw Exception("Categories null")
                emit(categories)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }
}