package com.joohnq.crosbooks.model.network.categories

import com.joohnq.crosbooks.model.entities.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CategoriesService {
    @GET("/categories")
    @Headers("Requires-Auth: true")
    suspend fun getCategories(): Response<List<Category>>
}