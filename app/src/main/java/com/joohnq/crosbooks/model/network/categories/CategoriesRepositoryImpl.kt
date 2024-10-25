package com.joohnq.crosbooks.model.network.categories

import com.joohnq.crosbooks.model.entities.Category
import com.joohnq.crosbooks.model.network.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CategoriesRepositoryImpl(
    private val categoriesService: CategoriesService,
    private val ioDispatcher: CoroutineDispatcher
) : CategoriesRepository {
    override fun getCategories(
    ): Flow<List<Category>> {
        return flow {
            try {
                val res = categoriesService.getCategories()
                val categories = NetworkHelper.handleNetworkErrors(res)
                emit(categories)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }
}