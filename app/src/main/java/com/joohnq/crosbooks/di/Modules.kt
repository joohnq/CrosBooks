package com.joohnq.crosbooks.di

import com.joohnq.crosbooks.constants.ApiConstants
import com.joohnq.crosbooks.model.entities.Book
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
import com.joohnq.crosbooks.view.permission.PermissionManager
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import com.joohnq.crosbooks.viewmodel.SearchViewModel
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
    singleOf(::getUserPreferencesDatastore)
    singleOf(::PermissionManager)
}

val viewModelModule = module {
    singleOf(::CategoriesViewModel)
    viewModelOf(::BooksViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserPreferencesViewModel)
    viewModelOf(::SearchViewModel)
}

val repositoryModule = module {
    singleOf(::UserPreferencesRepositoryImpl) bind UserPreferencesRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::BooksRepositoryImpl) bind BooksRepository::class
    singleOf(::CategoriesRepositoryImpl) bind CategoriesRepository::class
}

val networkModule = module {
    singleOf(::AuthInterceptor)
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
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
}

val servicesModule = module {
    single<AuthService> { get<Retrofit>().create(AuthService::class.java) }
    single<BooksService> { get<Retrofit>().create(BooksService::class.java) }
    single<CategoriesService> { get<Retrofit>().create(CategoriesService::class.java) }
}

val allModules = listOf(
    appModule,
    viewModelModule,
    repositoryModule,
    networkModule,
    servicesModule
)