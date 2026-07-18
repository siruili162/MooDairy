package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MoodDiaryTheme
import com.example.ui.viewmodel.DiaryViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
            MoodDiaryTheme(themeName = currentTheme) {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
