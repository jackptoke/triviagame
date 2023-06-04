package dev.agitek.triviagame.screens.questions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.agitek.triviagame.models.Question
import dev.agitek.triviagame.utils.AppColors


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    val questionIndex =  remember { mutableStateOf(0) }

    if(viewModel.data.value.loading == true) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(strokeWidth = 5.dp, color = Color.Yellow, modifier = Modifier.fillMaxWidth(0.2f))
        }
    }
    else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch(ex: Exception) {
            null
        }
        if(questions != null) {
            QuestionDisplay(question = question!!, questionIndex = questionIndex, viewModel = viewModel ) {
                questionIndex.value = questionIndex.value + 1
            }
        }
    }

}

@Preview
@Composable
fun QuestionDisplay(question: Question = Question(question = "What is the question?", answer = "I know the answer", category = "world", choices = listOf("Yes", "No")),
                    questionIndex: MutableState<Int> = remember { mutableStateOf(0) },
                    viewModel: QuestionsViewModel = hiltViewModel(),
                    onNextClicked: (Int) -> Unit = {}) {
    
    val choiceState = remember(question) {
        question.choices.toMutableList()
    }
    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choiceState[it] == question.answer
        }
    }
    
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top) {
            if(questionIndex.value >= 5) ShowProgress(score = questionIndex.value, total = viewModel.getTotalQuestionsCount())
            QuestionTracker(counter = questionIndex.value, total = viewModel.getTotalQuestionsCount())
            DrawDottedLine(PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            Column(modifier = Modifier.padding(top=10.dp, bottom = 10.dp)) {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite,
                    )
                choiceState.forEachIndexed { index, answerText ->
                    Row(modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(
                            width = 4.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColors.mOffDarkPurple,
                                    AppColors.mOffDarkPurple
                                )
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomEndPercent = 50,
                                bottomStartPercent = 50
                            )
                        )
                        .background(Color.Transparent)
                        ,
                        verticalAlignment = Alignment.CenterVertically) {

                        //Radio button
                        RadioButton(selected = (answerState.value == index), onClick = {
                            updateAnswer(index)
                        }, modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(selectedColor = if(correctAnswerState.value == true && index == answerState.value) Color.Green.copy(alpha = 0.4f) else Color.Red.copy(alpha = 0.4f)))
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light, color =
                            if(correctAnswerState.value == true && index == answerState.value) Color.Green
                            else if(correctAnswerState.value == false && index == answerState.value) Color.Red
                            else AppColors.mOffWhite, fontSize = 17.sp)) {
                                append(text = answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }

                Button(onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp), 
                    colors = buttonColors(containerColor = AppColors.mLightBlue)) {
                    Text(text = "Next",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                onNextClicked(questionIndex.value)
                            },
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10,
                    total: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)) {
                append("Question $counter/")

                withStyle(style = SpanStyle(color = AppColors.mLightGray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp)) {
                    append("$total")
                }
            }

        }
    }, modifier = Modifier.padding(20.dp))

}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        drawLine(color = AppColors.mLightGray,
            start = Offset.Zero,
            end = Offset(x = size.width, y= 0f),
            pathEffect = pathEffect)
    }
}

@Preview
@Composable
fun ShowProgress(score: Int = 12, total: Int = 100) {
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFBE6BE5)))
    val progressFactor by remember(score) {
        mutableStateOf(score * (1f / total ))
    }
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.mLightPurple,
                    AppColors.mLightPurple
                )
            ),
            shape = RoundedCornerShape(34.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomStartPercent = 50,
                bottomEndPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Button(contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent)
            ) {
            Text(text = (score).toString(), modifier = Modifier.clip(shape = RoundedCornerShape(23.dp)).fillMaxHeight(0.87f)
                .fillMaxWidth().padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center)
        }
    }
}