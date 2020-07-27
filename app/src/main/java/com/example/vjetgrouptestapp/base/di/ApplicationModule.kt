package com.example.vjetgrouptestapp.base.di

import android.content.Context
import androidx.room.Room
import com.example.vjetgrouptestapp.BuildConfig
import com.example.vjetgrouptestapp.base.db.AppDatabase
import com.example.vjetgrouptestapp.base.db.DatabaseDao
import com.example.vjetgrouptestapp.base.db.DbSettings
import com.example.vjetgrouptestapp.base.db.LocalRepository
import com.example.vjetgrouptestapp.base.arch.GeneralErrorHandle
import com.example.vjetgrouptestapp.base.remote.interceptors.HeaderInterceptor
import com.example.vjetgrouptestapp.base.remote.RemoteRepository
import com.example.vjetgrouptestapp.base.remote.RemoteSettings
import com.example.vjetgrouptestapp.ui.feeds.list.FeedPagingFactory
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideFeedPagingFactory(
        remoteRepository: RemoteRepository,
        localRepository: LocalRepository
    ): FeedPagingFactory {
        return FeedPagingFactory(
            remoteRepository,
            localRepository
        )
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(retrofit: Retrofit,generalErrorHandle: GeneralErrorHandle): RemoteRepository {
        return RemoteRepository(retrofit,generalErrorHandle)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(databaseDao: DatabaseDao): LocalRepository {
        return LocalRepository(databaseDao)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RemoteSettings.BASE_HTTP_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
        return appDatabase.databaseRepository()
    }

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DbSettings.DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun createClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            readTimeout(RemoteSettings.READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(RemoteSettings.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(RemoteSettings.WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(HeaderInterceptor())
        }
        if (BuildConfig.DEBUG) {
            val logInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(logInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun createGeneralErrorHandler(): GeneralErrorHandle {
        return GeneralErrorHandle()
    }
}