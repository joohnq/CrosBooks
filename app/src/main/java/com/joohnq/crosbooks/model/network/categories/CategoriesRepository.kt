package com.joohnq.crosbooks.model.network.categories

import com.joohnq.crosbooks.model.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategories(): Flow<List<Category>>
}