package dev.agitek.triviagame.network

import dev.agitek.triviagame.models.Questions
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getAllQuestions(): Questions
}