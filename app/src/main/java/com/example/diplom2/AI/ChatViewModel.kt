package com.example.diplom2.AI

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()

    private val apiKey =
        "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"

    fun sendMessage(userMessage: String) {

        messages.add(
            ChatMessage("user", userMessage)
        )

        viewModelScope.launch {

            try {

                val response =
                    RetrofitClient.api.sendMessage(
                        auth = "Bearer $apiKey",
                        referer = "https://yourapp.com",
                        title = "Diplom AI Assistant",

                        request = ChatRequest(
                            model = "nvidia/nemotron-3-super-120b-a12b:free",

                            messages = listOf(
                                ChatMessage(
                                    "system",
                                    """
Ты AI помощник приложения по обучению смартфонам.

Отвечай:
- простым языком
- пошагово
- понятно для новичков
- дружелюбно

Помогай:
- с настройками телефона
- вирусами
- Wi-Fi
- батареей
- приложениями
- камерой
- безопасностью
                                    """.trimIndent()
                                ),

                                ChatMessage(
                                    "user",
                                    userMessage
                                )
                            )
                        )
                    )

                val answer =
                    response.choices.firstOrNull()
                        ?.message?.content
                        ?: "Ошибка ответа AI"

                messages.add(
                    ChatMessage("assistant", answer)
                )

            } catch (e: Exception) {

                messages.add(
                    ChatMessage(
                        "assistant",
                        "Ошибка подключения к AI"
                    )
                )
            }
        }
    }
}