package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.ChatMessage
import com.example.data.DiaryEntry
import com.example.ui.viewmodel.DiaryViewModel
import java.util.Date

// Main App Tabs
enum class AppTab(val label: String) {
    DIARY("手账日记"),
    AI_CAVE("树洞对话"),
    CHARTS_SETTINGS("心情分析")
}

@Composable
fun MainScreen(viewModel: DiaryViewModel) {
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(AppTab.DIARY) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = selectedTab == AppTab.DIARY,
                    onClick = { selectedTab = AppTab.DIARY },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == AppTab.DIARY) Icons.Default.Book else Icons.Default.Book,
                            contentDescription = "手账日记",
                            modifier = Modifier.testTag("tab_diary")
                        )
                    },
                    label = { Text(AppTab.DIARY.label, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.AI_CAVE,
                    onClick = { selectedTab = AppTab.AI_CAVE },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == AppTab.AI_CAVE) Icons.Default.ChatBubble else Icons.Default.ChatBubble,
                            contentDescription = "树洞对话",
                            modifier = Modifier.testTag("tab_chat")
                        )
                    },
                    label = { Text(AppTab.AI_CAVE.label, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.CHARTS_SETTINGS,
                    onClick = { selectedTab = AppTab.CHARTS_SETTINGS },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == AppTab.CHARTS_SETTINGS) Icons.Default.AutoGraph else Icons.Default.Settings,
                            contentDescription = "心情分析与设置",
                            modifier = Modifier.testTag("tab_settings")
                        )
                    },
                    label = { Text(AppTab.CHARTS_SETTINGS.label, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = selectedTab,
            animationSpec = tween(300),
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                AppTab.DIARY -> DiaryScreen(viewModel)
                AppTab.AI_CAVE -> AiCaveScreen(viewModel)
                AppTab.CHARTS_SETTINGS -> ChartsAndSettingsScreen(viewModel)
            }
        }
    }
}

// ==================== DIARY TAB ====================

@Composable
fun DiaryScreen(viewModel: DiaryViewModel) {
    val diaries by viewModel.diaries.collectAsStateWithLifecycle()
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    var showWriteDialog by remember { mutableStateOf(false) }
    var selectedDiaryForDetail by remember { mutableStateOf<DiaryEntry?>(null) }

    val headerGradient = remember(currentTheme) {
        when (currentTheme) {
            "NATURAL" -> Brush.verticalGradient(listOf(Color(0xFF8C9C75), Color(0xFF5A5A40)))
            "FOREST" -> Brush.verticalGradient(listOf(Color(0xFF4E6C50), Color(0xFF2C3E2E)))
            "OCEAN" -> Brush.verticalGradient(listOf(Color(0xFF2E5B88), Color(0xFF1E2E3E)))
            "SAKURA" -> Brush.verticalGradient(listOf(Color(0xFFE87A90), Color(0xFF5E2F38)))
            else -> Brush.verticalGradient(listOf(Color(0xFFE07A5F), Color(0xFF3D405B)))
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                // Header Banner Style
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(headerGradient)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_diary_header),
                        contentDescription = "Header Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.35f
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "情绪手账",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "纸笔之间，情绪开花 🌸 记录真实的自己",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            if (diaries.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp, start = 32.dp, end = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "✏️",
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "还没有写过手账日记呢...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "点击下方的“+”号按钮，添加你的第一篇治愈手账吧！",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(diaries) { diary ->
                    DiaryListItem(
                        diary = diary,
                        onClick = { selectedDiaryForDetail = diary },
                        onDelete = { viewModel.deleteDiary(diary.id) }
                    )
                }
            }
        }

        // Add Floating Action Button
        FloatingActionButton(
            onClick = { showWriteDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_diary_fab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "撰写手账")
        }
    }

    // Write Diary Dialog
    if (showWriteDialog) {
        WriteDiaryDialog(
            viewModel = viewModel,
            onDismiss = { showWriteDialog = false }
        )
    }

    // Detail Dialog
    selectedDiaryForDetail?.let { diary ->
        DiaryDetailDialog(
            diary = diary,
            onDismiss = { selectedDiaryForDetail = null },
            onDelete = {
                viewModel.deleteDiary(diary.id)
                selectedDiaryForDetail = null
            }
        )
    }
}

@Composable
fun DiaryListItem(
    diary: DiaryEntry,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val dateString = remember(diary.date) {
        val date = Date(diary.date)
        DateFormat.format("yyyy年MM月dd日 HH:mm", date).toString()
    }

    // Match background style based on entry background theme
    val cardBgColor = when (diary.bgTheme) {
        "NATURAL" -> Color(0xFFF3F4ED) // Natural soft sage cream
        "FOREST" -> Color(0xFFEBF2EC)
        "OCEAN" -> Color(0xFFEDF4FC)
        "SAKURA" -> Color(0xFFFFF0F3)
        else -> Color(0xFFFFF5EB) // WARM SUN OR DEFAULT
    }

    val moodEmoji = getMoodEmoji(diary.mood)
    val moodLabel = getMoodLabel(diary.mood)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
            .testTag("diary_item_${diary.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mood Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White, CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = moodEmoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = diary.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$dateString | 今日情绪：$moodLabel",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body text
            Text(
                text = diary.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            // Stickers row
            if (diary.stickers.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    diary.stickers.split(",").forEach { sticker ->
                        if (sticker.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = sticker, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // AI Insight snippet (if available)
            if (diary.aiCommentary.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌲", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "小树仔评语: \"${diary.aiCommentary}\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WriteDiaryDialog(
    viewModel: DiaryViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("CALM") }
    var selectedBgTheme by remember { mutableStateOf("NATURAL") }
    val isAnalyzing by viewModel.isAnalyzingDiary.collectAsStateWithLifecycle()

    // Stickers selection
    val availableStickers = listOf("🌟", "❤️", "🍀", "🐱", "🎈", "🧁", "🌙", "🌸", "🍄", "🍪")
    val selectedStickers = remember { mutableStateListOf<String>() }

    Dialog(
        onDismissRequest = { if (!isAnalyzing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss, enabled = !isAnalyzing) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "创作治愈手账",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        } else {
                            Button(
                                onClick = {
                                    if (content.isNotBlank()) {
                                        viewModel.saveDiary(
                                            title = title,
                                            content = content,
                                            selectedMood = selectedMood,
                                            selectedBgTheme = selectedBgTheme,
                                            stickersList = selectedStickers.toList(),
                                            onSuccess = onDismiss
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                enabled = content.isNotBlank(),
                                modifier = Modifier.testTag("save_diary_button")
                            ) {
                                Text("保存", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Title Field
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("给今天起个温馨标题（选填）") },
                            placeholder = { Text("例：惬意的下午茶") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            enabled = !isAnalyzing
                        )
                    }

                    item {
                        // Content Field
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            label = { Text("写下你的心里话...") },
                            placeholder = { Text("今天发生了什么事？有什么想法、开心或烦恼都可以尽情记在这里喔，树洞会为你分析情绪趋势。") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("diary_content_input"),
                            shape = RoundedCornerShape(16.dp),
                            maxLines = 10,
                            enabled = !isAnalyzing
                        )
                    }

                    item {
                        // Mood Picker Title
                        Text("今日情绪指数", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(
                                Pair("HAPPY", "😊"),
                                Pair("CALM", "😌"),
                                Pair("SAD", "😢"),
                                Pair("ANXIOUS", "😰"),
                                Pair("ANGRY", "😡")
                            ).forEach { (mood, emoji) ->
                                val isSelected = selectedMood == mood
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                            shape = CircleShape
                                        )
                                        .clickable(enabled = !isAnalyzing) { selectedMood = mood },
                                    contentAlignment = Alignment.Center
                               ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = emoji, fontSize = 26.sp)
                                        Text(
                                            text = getMoodLabel(mood),
                                            fontSize = 9.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Theme Picker
                        Text("手账背景风格", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(
                                Pair("NATURAL", "自然绿"),
                                Pair("WARM", "温暖橘"),
                                Pair("FOREST", "森林绿"),
                                Pair("OCEAN", "深海蓝"),
                                Pair("SAKURA", "樱花粉")
                            ).forEach { (themeKey, name) ->
                                val isSelected = selectedBgTheme == themeKey
                                val colorSample = when (themeKey) {
                                    "NATURAL" -> Color(0xFFD4E2D5)
                                    "FOREST" -> Color(0xFFC4DFB8)
                                    "OCEAN" -> Color(0xFFAFD1F3)
                                    "SAKURA" -> Color(0xFFF9C7D5)
                                    else -> Color(0xFFFED8B1)
                                }
                                item {
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable(enabled = !isAnalyzing) { selectedBgTheme = themeKey }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(colorSample, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = name,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Stickers picker
                        Text("添加手账小贴纸 🧸", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                // List active stickers
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("已选用贴纸：", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    if (selectedStickers.isEmpty()) {
                                        Text("无（点击下方贴纸可添加）", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                                    } else {
                                        selectedStickers.forEach { sticker ->
                                            Text(
                                                text = sticker,
                                                fontSize = 16.sp,
                                                modifier = Modifier.clickable(enabled = !isAnalyzing) { selectedStickers.remove(sticker) }
                                            )
                                        }
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 10.dp))
                                // Available stickers to select
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    availableStickers.forEach { sticker ->
                                        val isChosen = selectedStickers.contains(sticker)
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    if (isChosen) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                                    CircleShape
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    CircleShape
                                                )
                                                .clickable(enabled = !isAnalyzing) {
                                                    if (isChosen) {
                                                        selectedStickers.remove(sticker)
                                                    } else {
                                                        if (selectedStickers.size < 5) { // Max 5 stickers
                                                            selectedStickers.add(sticker)
                                                        }
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = sticker, fontSize = 20.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isAnalyzing) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "小树仔正在精心读解你的心境，分析趋势中...",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Simple FlowRow helper since standard foundation FlowRow might be in experimental
@Composable
fun FlowRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // Basic Row layout that accommodates multiple elements
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun DiaryDetailDialog(
    diary: DiaryEntry,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val dateString = remember(diary.date) {
        val date = Date(diary.date)
        DateFormat.format("yyyy年MM月dd日 HH:mm", date).toString()
    }

    val cardBgColor = when (diary.bgTheme) {
                                "NATURAL" -> Color(0xFFF5F6F0) // Natural soft sage cream detail
                                "FOREST" -> Color(0xFFF0F6F1)
                                "OCEAN" -> Color(0xFFF3F7FC)
                                "SAKURA" -> Color(0xFFFFFAFB)
                                else -> Color(0xFFFFF9F3) // WARM SUN
                            }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color.White, CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = getMoodEmoji(diary.mood), fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = diary.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dateString,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "删除",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                // Scrollable Journal content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            Text(
                                text = diary.content,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                // Decorative Stickers
                if (diary.stickers.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        diary.stickers.split(",").forEach { sticker ->
                            if (sticker.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = sticker, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }

                // AI Warm Commentary
                if (diary.aiCommentary.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "🌲", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "小树仔情绪解析",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = diary.aiCommentary,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("返回手账架", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun getMoodEmoji(mood: String): String {
    return when (mood) {
        "HAPPY" -> "😊"
        "CALM" -> "😌"
        "SAD" -> "😢"
        "ANXIOUS" -> "😰"
        "ANGRY" -> "😡"
        else -> "😌"
    }
}

fun getMoodLabel(mood: String): String {
    return when (mood) {
        "HAPPY" -> "欢喜开怀"
        "CALM" -> "安宁静雅"
        "SAD" -> "失落忧郁"
        "ANXIOUS" -> "惶恐纠结"
        "ANGRY" -> "心烦意乱"
        else -> "心如止水"
    }
}

// ==================== AI CAVE TAB ====================

@Composable
fun AiCaveScreen(viewModel: DiaryViewModel) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isSending by viewModel.isSendingChatMessage.collectAsStateWithLifecycle()
    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Quick dialogue prompts
    val quickPrompts = listOf(
        "感觉好焦虑，不知道该怎么办 😣",
        "今天发生了特别开心的事！✨",
        "小树仔，帮我排解一下压力吧 🌿",
        "有些秘密不知道和谁诉说... 💭"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        // Cave Header Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌲", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "树洞守护者：小树仔", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "倾听所有情绪，无条件接纳你的树洞空间",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = { viewModel.clearChatHistory() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "清空对话",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // Chat list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Quick guidance prompts on top
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "💡 试着点击快速倾诉：",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickPrompts.forEach { prompt ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                                    .clickable(enabled = !isSending) {
                                        viewModel.sendChatMessage(prompt)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = prompt, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(messages) { message ->
                ChatMessageBubble(message = message)
            }

            if (isSending) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("小树仔正在沙沙写信...", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Bottom input
        Surface(
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text("对着树洞说点悄悄话吧...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.sendChatMessage(inputMessage)
                            inputMessage = ""
                            keyboardController?.hide()
                        }
                    }),
                    enabled = !isSending
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.sendChatMessage(inputMessage)
                            inputMessage = ""
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier.size(48.dp).testTag("chat_send_button"),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "发送",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.sender == "USER"
    val bubbleColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderMod = if (isUser) Modifier else Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌲", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .then(borderMod)
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐱", fontSize = 18.sp)
            }
        }
    }
}

// ==================== CHARTS & SETTINGS TAB ====================

@Composable
fun ChartsAndSettingsScreen(viewModel: DiaryViewModel) {
    val diaries by viewModel.diaries.collectAsStateWithLifecycle()
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()

    var apiKeyInput by remember { mutableStateOf(customApiKey) }
    var showKeySavedIndicator by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📊 情绪波动趋势图",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "通过AI自动解读您的手账分析心情波峰，展现近期的身心状态：",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Mood Trend Chart
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (diaries.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无心情数据，去写篇手账吧~ 📝",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        MoodTrendCanvas(diaries = diaries)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Brief emotional analysis summary
                    val diarySummaryText = remember(diaries) {
                        if (diaries.isEmpty()) {
                            "开始记录你的每一天吧，小树洞会根据日记内容，分析你的情绪走向并生成周波曲线。"
                        } else {
                            val averageScore = diaries.map { it.moodValue }.average()
                            val happyCount = diaries.count { it.mood == "HAPPY" }
                            val sadCount = diaries.count { it.mood == "SAD" }
                            
                            val analysisWord = when {
                                averageScore >= 4.0 -> "情绪状态极佳！近期你身边有很多温暖美好的小确幸，记得常怀感恩，保持当下的自在愉悦喔 ✨"
                                averageScore >= 3.0 -> "近期心态平和稳定。在波澜不惊的日常里，偶尔也可以犒赏一下自己，去大自然中走走 🍃"
                                else -> "近期似乎有些疲累或者情绪低潮。别太焦虑，阴雨天总会过去的，多喝热水，小树仔会在这里一直拥抱你 🧸"
                            }
                            "您已记录了 ${diaries.size} 篇手账。平均情绪指数：${String.format("%.1f", averageScore)}。其中开怀快乐 ${happyCount} 次，低落难过 ${sadCount} 次。\n\n$analysisWord"
                        }
                    }

                    Text(
                        text = diarySummaryText,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "🎨 个性主题切换",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Theme picker Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val themeOptions = listOf(
                    Triple("NATURAL", "大地自然", Color(0xFF8C9C75)),
                    Triple("WARM", "治愈暖阳", Color(0xFFE07A5F)),
                    Triple("FOREST", "静谧森林", Color(0xFF4E6C50)),
                    Triple("OCEAN", "深海树洞", Color(0xFF2E5B88)),
                    Triple("SAKURA", "浪漫樱花", Color(0xFFE87A90))
                )

                themeOptions.forEach { (themeKey, name, color) ->
                    val isSelected = currentTheme == themeKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.updateTheme(themeKey) }
                            .padding(12.dp)
                            .testTag("theme_btn_$themeKey"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(color, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "🔑 开发者 API 配置",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Custom API Key Settings
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "本软件支持调用 Gemini-3.5-Flash 进行AI分析和对话。默认已包含试用内置 API Key。如果您有自己的 Google AI Key，可在下方输入自定义激活您的专属服务（极速且免去并发限流哦！）：",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "当前授权状态：", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        if (customApiKey.isBlank()) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFEBFDF2), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "系统试用内置 Key 激活中", fontSize = 10.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "用户专属 API Key 已就绪", fontSize = 10.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        label = { Text("Gemini API Key") },
                        placeholder = { Text("AI_zaSy...") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("api_key_field"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showKeySavedIndicator) {
                            Text(
                                text = "✓ 保存成功",
                                fontSize = 12.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Button(
                            onClick = {
                                viewModel.updateApiKey(apiKeyInput)
                                showKeySavedIndicator = true
                            },
                            modifier = Modifier.testTag("api_key_save_btn")
                        ) {
                            Text("更新 API Key", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mandatory security extraction warning as specified in skill instructions
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "🔒 Security Warning: I have included your API keys in the generated APK file for this prototype. Please be aware that Android APKs can be easily decompiled, and these keys can be extracted by anyone who has access to the file. Do not share this APK file publicly or with unauthorized individuals to prevent potential misuse.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoodTrendCanvas(diaries: List<DiaryEntry>) {
    // Take the last 7 entries and sort from oldest to newest
    val recentDiaries = remember(diaries) {
        diaries.take(7).reversed()
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    // Custom Canvas drawing for the beautiful Mood Trend Line
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 16.dp, bottom = 16.dp, start = 12.dp, end = 12.dp)
    ) {
        val width = size.width
        val height = size.height

        // Min-Max limits for score range (1.0 to 5.0)
        val minScore = 1.0f
        val maxScore = 5.0f
        val scoreRange = maxScore - minScore

        val numPoints = recentDiaries.size
        val xSpacing = if (numPoints > 1) width / (numPoints - 1) else width

        // Calculate positions
        val points = recentDiaries.mapIndexed { index, diary ->
            val x = index * xSpacing
            // Invert Y so that higher scores are at the top (smaller Y-coordinate value)
            val normalizedScore = (diary.moodValue - minScore) / scoreRange
            val y = height - (normalizedScore * height)
            Offset(x, y)
        }

        // 1. Draw horizontal reference grid lines
        val numGridLines = 5
        for (i in 0 until numGridLines) {
            val y = height * i / (numGridLines - 1)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (points.isNotEmpty()) {
            // 2. Draw gradient fill underneath the line
            val fillPath = Path().apply {
                moveTo(points.first().x, height)
                points.forEach { offset ->
                    lineTo(offset.x, offset.y)
                }
                lineTo(points.last().x, height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.3f),
                        primaryColor.copy(alpha = 0.0f)
                    )
                )
            )

            // 3. Draw connecting line
            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }

            drawPath(
                path = linePath,
                color = primaryColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // 4. Draw circular dots
            points.forEachIndexed { index, offset ->
                drawCircle(
                    color = Color.White,
                    radius = 5.dp.toPx(),
                    center = offset
                )
                drawCircle(
                    color = primaryColor,
                    radius = 3.dp.toPx(),
                    center = offset
                )
            }
        }
    }

    // Horizontal captions
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val recentDiaries = diaries.take(7).reversed()
        recentDiaries.forEach { diary ->
            val shortDate = remember(diary.date) {
                val date = Date(diary.date)
                DateFormat.format("MM/dd", date).toString()
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = shortDate, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text(text = getMoodEmoji(diary.mood), fontSize = 11.sp)
            }
        }
    }
}
