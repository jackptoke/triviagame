package dev.agitek.triviagame.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.agitek.triviagame.network.QuestionApi
import dev.agitek.triviagame.repos.QuestionRepository
import dev.agitek.triviagame.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionApi) = QuestionRepository(api)


    @Singleton
    @Provides
    fun provideQuestionApi(): QuestionApi {
        Log.d("AppModule", "provideQuestionApi")
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}