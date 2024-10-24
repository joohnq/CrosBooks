package com.joohnq.crosbooks.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.joohnq.crosbooks.constants.ApiConstants
import com.joohnq.crosbooks.model.local.UserPreferencesRepository
import com.joohnq.crosbooks.model.local.UserPreferencesRepositoryImpl
import com.joohnq.crosbooks.model.local.getUserPreferencesDatastore
import com.joohnq.crosbooks.model.network.auth.AuthInterceptor
import com.joohnq.crosbooks.model.network.auth.AuthRepository
import com.joohnq.crosbooks.model.network.auth.AuthRepositoryImpl
import com.joohnq.crosbooks.model.network.auth.AuthService
import com.joohnq.crosbooks.model.network.books.BooksRepository
import com.joohnq.crosbooks.model.network.books.BooksRepositoryImpl
import com.joohnq.crosbooks.model.network.books.BooksService
import com.joohnq.crosbooks.model.network.categories.CategoriesRepository
import com.joohnq.crosbooks.model.network.categories.CategoriesRepositoryImpl
import com.joohnq.crosbooks.model.network.categories.CategoriesService
import com.joohnq.crosbooks.model.network.SwaggerDataSource
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import com.joohnq.crosbooks.viewmodel.UserPreferencesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    singleOf<CoroutineDispatcher>(Dispatchers::IO)
    single<DataStore<Preferences>> { getUserPreferencesDatastore(get()) }
}

val viewModelModule = module {
    singleOf(::CategoriesViewModel)
    viewModelOf(::BooksViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserPreferencesViewModel)
}

val repositoryModule = module {
    single { UserPreferencesRepositoryImpl(get(), get()) } bind UserPreferencesRepository::class
    single { AuthRepositoryImpl(get(), get()) } bind AuthRepository::class
    single { BooksRepositoryImpl(get(), get()) } bind BooksRepository::class
    single { CategoriesRepositoryImpl(get(), get()) } bind CategoriesRepository::class
}

val networkModule = module {
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single<AuthInterceptor> {
        AuthInterceptor(get<UserPreferencesRepository>())
    }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }
    single<Retrofit> {
        Retrofit
            .Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<SwaggerDataSource> { SwaggerDataSource(get()) }
}

val servicesModule = module {
    single<AuthService> { get<SwaggerDataSource>().authService }
    single<BooksService> { get<SwaggerDataSource>().booksService }
    single<CategoriesService> { get<SwaggerDataSource>().categoriesService }
}

val allModules = listOf(
    appModule,
    viewModelModule,
    repositoryModule,
    networkModule,
    servicesModule
)