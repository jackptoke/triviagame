package dev.agitek.triviagame.screens.questions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agitek.triviagame.data.DataOrException
import dev.agitek.triviagame.models.Question
import dev.agitek.triviagame.repos.QuestionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repo: QuestionRepository): ViewModel() {
    val data: MutableState<DataOrException<ArrayList<Question>, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repo.getAllQuestions()
            if(data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
                Log.d("QuestionsViewModel", "GetAllQuestions")
            }

        }
    }

    fun getTotalQuestionsCount(): Int {
        return data.value.data?.toMutableList()?.size!!
    }


}