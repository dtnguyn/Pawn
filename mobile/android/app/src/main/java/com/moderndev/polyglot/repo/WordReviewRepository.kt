package com.moderndev.polyglot.repo

import com.moderndev.polyglot.db.PolyglotDatabase
import com.moderndev.polyglot.db.mapper.SavedWordMapper
import com.moderndev.polyglot.model.ReviewQuestion
import com.moderndev.polyglot.util.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.math.max
import kotlin.random.Random


class WordReviewRepository(
    private val db: PolyglotDatabase
) {

    private val questionTypes = listOf("definition", "word", "audio")
    private val questionTypesWithoutAudio = listOf("definition", "word", "audio")


    suspend fun getReviewQuestions(language: String, questionCount: Int? = null): Flow<UIState<List<ReviewQuestion>>> {
        return flow {
            emit(UIState.Loading())
            val savedWords = db.savedWordDao().getMany(language).first()

            val randomWords =
                savedWords.asSequence().shuffled().take(max(savedWords.size, questionCount ?: 0)).toList()

            val questions = arrayListOf<ReviewQuestion>()

            for (word in randomWords) {
                val randomQuestionType = if (word.pronunciationAudio != null) {
                    questionTypes[Random.nextInt(questionTypes.size)]
                } else {
                    questionTypes[Random.nextInt(questionTypesWithoutAudio.size)]
                }

                when (randomQuestionType) {
                    "definition" -> {
                        val answerOptions =
                            savedWords.filter { it.value != word.value }.asSequence().shuffled()
                                .take(3).toList().map { it.value }

                        questions.add(
                            ReviewQuestion(
                                word = SavedWordMapper.mapToNetworkEntity(word),
                                question = "Which of the following words has this definition: \n \"${word.mainDefinition}\"",
                                answerOptions = (answerOptions + listOf(word.value)).shuffled(),
                                correctAnswer = word.value,
                                userAnswer = null,
                                type = "definition"
                            )
                        )
                    }
                    "word" -> {
                        val answerOptions =
                            savedWords.filter { it.value != word.value }.asSequence().shuffled()
                                .take(3).toList().map { it.mainDefinition }

                        questions.add(
                            ReviewQuestion(
                                word = SavedWordMapper.mapToNetworkEntity(word),
                                question = "What is the definition of \"${word.value}\"",
                                answerOptions = (answerOptions + listOf(word.mainDefinition)).shuffled(),
                                correctAnswer = word.mainDefinition,
                                userAnswer = null,
                                type = "word"
                            )
                        )
                    }
                    else -> {
                        val answerOptions =
                            savedWords.filter { it.value != word.value }.asSequence().shuffled()
                                .take(3).toList().map { it.mainDefinition }

                        questions.add(
                            ReviewQuestion(
                                word = SavedWordMapper.mapToNetworkEntity(word),
                                question = "What is the definition of the word in the following audio?",
                                answerOptions = (answerOptions + listOf(word.mainDefinition)).shuffled(),
                                correctAnswer = word.mainDefinition,
                                userAnswer = null,
                                type = "audio"
                            )
                        )
                    }
                }
            }
            emit(UIState.Loaded(questions.toList()))
        }

    }

}

