package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodapp.data.db.CategoryDao
import com.example.foodapp.data.db.DataBase
import com.example.foodapp.data.db.RecipeDao
import com.example.foodapp.data.services.RecipesService
import com.example.foodapp.utils.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providerGson(): Gson =
        GsonBuilder().create()

    @Provides
    @Singleton
    fun providerGsonConvertFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun provideRecipesService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): RecipesService {
        return providerService(okHttpClient, gsonConverterFactory, RecipesService::class.java)
    }

    private fun <T> providerService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        clazz: Class<T>,
    ): T =
        createRetrofit(okHttpClient, gsonConverterFactory).create(clazz)

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()


    @Provides
    @Singleton
    fun providerLoggingInterceptor() : HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun provideMealDao(myDatabase: DataBase): CategoryDao{
        return myDatabase.categoryDao()
    }

    @Provides
    fun provideCartDao(myDatabase: DataBase): RecipeDao{
        return myDatabase.recipeDao()
    }

    @Provides
    @Singleton
    fun provideMyDatabase(@ApplicationContext appContext: Context): DataBase {
        return Room.databaseBuilder(
            appContext,
            DataBase::class.java,
            "DataBase"
        ).build()
    }
}