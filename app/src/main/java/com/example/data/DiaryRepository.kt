package com.example.data

import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val db: MoodDiaryDatabase) {
    val allDiaries: Flow<List<DiaryEntry>> = db.diaryDao().getAllDiaries()
    val allChatMessages: Flow<List<ChatMessage>> = db.chatDao().getAllMessages()

    suspend fun insertDiary(entry: DiaryEntry) {
        db.diaryDao().insertDiary(entry)
    }

    suspend fun deleteDiaryById(id: Int) {
        db.diaryDao().deleteDiaryById(id)
    }

    suspend fun insertChatMessage(message: ChatMessage) {
        db.chatDao().insertMessage(message)
    }

    suspend fun clearChat() {
        db.chatDao().clearChat()
    }
}
