package com.example.diplom2.AI

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplom2.BuildConfig
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()

    // Ключ из BuildConfig (безопасно)
    private val apiKey = BuildConfig.OPENROUTER_API_KEY

    init {
        // Для отладки: если ключ пустой, вы увидите ошибку в логах
        if (apiKey.isBlank()) {
            android.util.Log.e("ChatViewModel", "OPENROUTER_API_KEY is empty! Check secrets.properties")
        }
    }

    fun sendMessage(userMessage: String) {
        messages.add(ChatMessage("user", userMessage))

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.sendMessage(
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
                            ChatMessage("user", userMessage)
                        )
                    )
                )
                val answer = response.choices.firstOrNull()?.message?.content ?: "Ошибка ответа AI"
                messages.add(ChatMessage("assistant", answer))
            } catch (e: Exception) {
                messages.add(ChatMessage("assistant", "Ошибка подключения к AI: ${e.message}"))
            }
        }
    }
}