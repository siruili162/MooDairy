package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // timestamp
    val title: String,
    val content: String,
    val mood: String, // HAPPY, CALM, SAD, ANXIOUS, ANGRY
    val moodValue: Float, // 1.0 to 5.0
    val bgTheme: String = "WARM", // WARM, FOREST, OCEAN, SAKURA
    val stickers: String = "", // comma separated stickers e.g., "star,heart"
    val aiCommentary: String = "" // AI warm reflection
)
