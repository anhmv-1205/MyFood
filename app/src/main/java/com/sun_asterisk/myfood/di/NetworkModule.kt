package com.sun_asterisk.myfood.di

import android.app.Application
import com.sun_asterisk.myfood.BuildConfig
import com.sun_asterisk.myfood.data.remote.api.middleware.InterceptorImpl
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.utils.Constant
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

var networkModule = module {
    single { provideOkHttpCache(androidApplication()) }
    single { provideInterceptor(get()) }
    single { provideOkHttpClient(get(), get()) }
    single { provideRetrofit(get()) }
    single { provideMyFoodApi(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient).build()
}

fun provideOkHttpCache(app: Application): Cache {
    val cacheSize: Long = 10 * 1024 * 1024
    return Cache(app.cacheDir, cacheSize)
}

fun provideInterceptor(tokenRepository: TokenRepository): Interceptor {
    return InterceptorImpl(tokenRepository)
}

fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder().apply {
        this.cache(cache)
        addInterceptor(interceptor)
        readTimeout(Constant.READ_TIMEOUT, SECONDS)
        writeTimeout(Constant.WRITE_TIMEOUT, SECONDS)
        connectTimeout(Constant.CONNECTION_TIMEOUT, SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            addInterceptor(logging)
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
    }.build()
}

fun provideMyFoodApi(retrofit: Retrofit): MyFoodApi = retrofit.create(MyFoodApi::class.java)
