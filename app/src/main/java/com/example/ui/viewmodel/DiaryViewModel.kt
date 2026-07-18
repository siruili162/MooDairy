package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatMessage
import com.example.data.DiaryEntry
import com.example.data.DiaryRepository
import com.example.data.MoodDiaryDatabase
import com.example.data.api.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DiaryRepository
    private val prefs = application.getSharedPreferences("mood_diary_prefs", Context.MODE_PRIVATE)

    val diaries: StateFlow<List<DiaryEntry>>
    val chatMessages: StateFlow<List<ChatMessage>>

    private val _currentTheme = MutableStateFlow(prefs.getString("selected_theme", "NATURAL") ?: "NATURAL")
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    private val _currentFontScale = MutableStateFlow(prefs.getFloat("font_scale", 1.0f))
    val currentFontScale: StateFlow<Float> = _currentFontScale.asStateFlow()

    private val _customApiKey = MutableStateFlow(prefs.getString("custom_api_key", "") ?: "")
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    private val _isAnalyzingDiary = MutableStateFlow(false)
    val isAnalyzingDiary: StateFlow<Boolean> = _isAnalyzingDiary.asStateFlow()

    private val _isSendingChatMessage = MutableStateFlow(false)
    val isSendingChatMessage: StateFlow<Boolean> = _isSendingChatMessage.asStateFlow()

    init {
        val database = MoodDiaryDatabase.getDatabase(application)
        repository = DiaryRepository(database)
        diaries = repository.allDiaries.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        chatMessages = repository.allChatMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial AI welcome message if chat is empty
        viewModelScope.launch {
            repository.allChatMessages.collect { list ->
                if (list.isEmpty()) {
                    repository.insertChatMessage(
                        ChatMessage(
                            sender = "AI",
                            message = "你好呀！我是你的小树洞AI助手“小树仔”~ 🌲✨\n在这里你可以倾吐你的任何烦恼，或者分享生活中的开心瞬间。我会一直守候在这里倾听你的声音。今天心情怎么样？",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    fun saveDiary(
        title: String,
        content: String,
        selectedMood: String,
        selectedBgTheme: String,
        stickersList: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isAnalyzingDiary.value = true
            try {
                // Call Gemini to analyze diary and get score + advice
                val apiKey = _customApiKey.value.ifBlank { null }
                val analysis = GeminiClient.analyzeDiary(content, apiKey)

                // If user manually chose a mood, we can prioritize theirs but use the AI moodValue & advice,
                // or just merge them. Let's merge:
                // If user didn't change default mood, we use the analyzed mood.
                // Otherwise we use user mood but keep the AI score & advice.
                val moodToSave = if (selectedMood == "CALM" && analysis.mood != "CALM") {
                    analysis.mood
                } else {
                    selectedMood
                }

                val entry = DiaryEntry(
                    date = System.currentTimeMillis(),
                    title = title.ifBlank { "无标题手账" },
                    content = content,
                    mood = moodToSave,
                    moodValue = analysis.score,
                    bgTheme = selectedBgTheme,
                    stickers = stickersList.joinToString(","),
                    aiCommentary = analysis.advice
                )

                repository.insertDiary(entry)
                onSuccess()
            } catch (e: Exception) {
                // Fallback direct save if network failure
                val entry = DiaryEntry(
                    date = System.currentTimeMillis(),
                    title = title.ifBlank { "无标题手账" },
                    content = content,
                    mood = selectedMood,
                    moodValue = when (selectedMood) {
                        "HAPPY" -> 4.8f
                        "CALM" -> 3.5f
                        "SAD" -> 1.8f
                        "ANXIOUS" -> 2.5f
                        "ANGRY" -> 2.0f
                        else -> 3.0f
                    },
                    bgTheme = selectedBgTheme,
                    stickers = stickersList.joinToString(","),
                    aiCommentary = "倾听你的心声，小树仔一直在这里。"
                )
                repository.insertDiary(entry)
                onSuccess()
            } finally {
                _isAnalyzingDiary.value = false
            }
        }
    }

    fun deleteDiary(id: Int) {
        viewModelScope.launch {
            repository.deleteDiaryById(id)
        }
    }

    fun sendChatMessage(messageContent: String) {
        if (messageContent.isBlank()) return
        viewModelScope.launch {
            val userMsg = ChatMessage(
                sender = "USER",
                message = messageContent,
                timestamp = System.currentTimeMillis()
            )
            repository.insertChatMessage(userMsg)
            _isSendingChatMessage.value = true

            try {
                // Get chat history up to 15 turns
                val currentHistory = chatMessages.value + userMsg
                val apiKey = _customApiKey.value.ifBlank { null }
                
                val aiReplyText = GeminiClient.getChatResponse(currentHistory, apiKey)
                
                repository.insertChatMessage(
                    ChatMessage(
                        sender = "AI",
                        message = aiReplyText,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                repository.insertChatMessage(
                    ChatMessage(
                        sender = "AI",
                        message = "唔……网络好像有点问题，小树仔暂时迷失了（错误: ${e.localizedMessage}）。但我依然会在这里默默守护你！",
                        timestamp = System.currentTimeMillis()
                    )
                )
            } finally {
                _isSendingChatMessage.value = false
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun updateTheme(theme: String) {
        _currentTheme.value = theme
        prefs.edit().putString("selected_theme", theme).apply()
    }

    fun updateFontScale(scale: Float) {
        _currentFontScale.value = scale
        prefs.edit().putFloat("font_scale", scale).apply()
    }

    fun updateApiKey(apiKey: String) {
        _customApiKey.value = apiKey
        prefs.edit().putString("custom_api_key", apiKey).apply()
    }
}
