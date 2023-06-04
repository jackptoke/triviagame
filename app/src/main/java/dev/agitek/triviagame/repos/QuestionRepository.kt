package dev.agitek.triviagame.repos

import android.util.Log
import dev.agitek.triviagame.data.DataOrException
import dev.agitek.triviagame.models.Question
import dev.agitek.triviagame.models.Questions
import dev.agitek.triviagame.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataDto = DataOrException<ArrayList<Question>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<Question>, Boolean, Exception> {
        try {
            dataDto.loading = true
            dataDto.data = api.getAllQuestions()

            if(dataDto.data.toString().isNotEmpty()) dataDto.loading = false
            Log.d("QuestionRepo", "getAllQuestions - ${(dataDto.data as Questions).size}")
        }catch (exception: Exception) {
            dataDto.exception = exception
            Log.d("QuestionRepo", "getAllQuestions - exception ${exception.message}")
        }
        return dataDto
    }
}