package com.joohnq.crosbooks.model.network

import com.joohnq.crosbooks.model.network.auth.AuthService
import com.joohnq.crosbooks.model.network.books.BooksService
import com.joohnq.crosbooks.model.network.categories.CategoriesService
import retrofit2.Retrofit

class SwaggerDataSource(private val retrofit: Retrofit) {
    val authService: AuthService = retrofit.create(AuthService::class.java)
    val booksService: BooksService = retrofit.create(BooksService::class.java)
    val categoriesService: CategoriesService = retrofit.create(CategoriesService::class.java)
}