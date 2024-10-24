package com.joohnq.crosbooks.model.network.books

import com.joohnq.crosbooks.model.entities.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface BooksService {
    @GET("/books")
    @Headers("Requires-Auth: true")
    suspend fun getBooks(): Response<BooksResponse>
}