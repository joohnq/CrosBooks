package com.joohnq.crosbooks.model.network.books

import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.BookImageResponse
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.entities.BooksResponse
import com.joohnq.crosbooks.model.entities.UserLoginPost
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksService {
    @GET("/books")
    @Headers("Requires-Auth: true")
    suspend fun getBooks(
        @Query("page") page: Int
    ): Response<BooksResponse>

    @GET("/books")
    @Headers("Requires-Auth: true")
    suspend fun getBooks(
        @Query("search") search: String,
        @Query("page") page: Int
    ): Response<BooksResponse>

    @POST("/upload-file")
    @Multipart
    @Headers("Requires-Auth: true")
    suspend fun sendBookImage(
        @Part file: MultipartBody.Part
    ): Response<BookImageResponse>

    @POST("/books")
    @Headers("Requires-Auth: true")
    suspend fun addBook(
        @Body bookPost: BookPost
    ): Response<Book>

    @DELETE("/books/{id}")
    @Headers("Requires-Auth: true")
    suspend fun removeBook(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("/books/{id}")
    @Headers("Requires-Auth: true")
    suspend fun getBookDetail(
        @Path("id") id: Int
    ): Response<Book>
}