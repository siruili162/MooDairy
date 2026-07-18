package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.DiaryEntry
import com.example.ui.viewmodel.DiaryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ToolsScreen(viewModel: DiaryViewModel) {
    var selectedTool by remember { mutableStateOf<String?>(null) }
    val diaries by viewModel.diaries.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Banner Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "治愈生活小助手 🌲",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "小树林为您准备的解压工具包，助你驱散焦虑，安抚情绪，拥抱平稳的心灵港湾。",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                lineHeight = 18.sp
                            )
                        }
                        Text(
                            text = "💡",
                            fontSize = 36.sp,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }

            // Section 1: Main Analytical & Query Tools
            item {
                Text(
                    text = "📊 心灵回顾与检索",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tool Button 1: Annual/Monthly Report
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clickable { selectedTool = "REPORT" }
                            .testTag("tool_report"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFEBF8FF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📅", fontSize = 18.sp)
                            }
                            Column {
                                Text(
                                    text = "年/月度回顾报告",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "统计复盘情感走势",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    // Tool Button 2: Find Diary by Date
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clickable { selectedTool = "FIND_DATE" }
                            .testTag("tool_find_date"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFFEF3C7), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔍", fontSize = 18.sp)
                            }
                            Column {
                                Text(
                                    text = "查阅指定日记",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "根据特定年月日检索",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Interactive Healing Tools (5+ items)
            item {
                Text(
                    text = "🧘 互动减压疗愈舱",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // 6 Interactive Tools Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val interactiveTools = listOf(
                        InteractiveToolItem("MUYU", "电子静心木鱼", "功德+1，平息内心烦躁 🐟", "🐟", Color(0xFFF0FDF4)),
                        InteractiveToolItem("BREATHE", "深呼吸引导舱", "4-4-4节奏调节焦虑情绪 🧘", "🧘", Color(0xFFEFF6FF)),
                        InteractiveToolItem("NOISE", "疗愈白噪音伴侣", "窗外雨声与森林篝火混音 🌧️", "🌧️", Color(0xFFFDF2F8)),
                        InteractiveToolItem("BOOK", "治愈答案之书", "翻开封印，解答内心困惑 📖", "📖", Color(0xFFFFF7ED)),
                        InteractiveToolItem("THERMO", "压力测试温度计", "快速自我身心评估与处方 🌡️", "🌡️", Color(0xFFF5F3FF)),
                        InteractiveToolItem("CARDS", "正念灵感日历卡", "治愈语录生成，放松心灵 ✨", "✨", Color(0xFFEDFDFD))
                    )

                    // Displaying in blocks of 2 items per row
                    for (i in interactiveTools.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (j in 0..1) {
                                if (i + j < interactiveTools.size) {
                                    val tool = interactiveTools[i + j]
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(125.dp)
                                            .clickable { selectedTool = tool.id }
                                            .testTag("tool_${tool.id.lowercase()}"),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(14.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(34.dp)
                                                        .background(tool.bgColor, CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(tool.icon, fontSize = 16.sp)
                                                }
                                                Icon(
                                                    imageVector = Icons.Default.ChevronRight,
                                                    contentDescription = "打开",
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = tool.title,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = tool.desc,
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    lineHeight = 13.sp
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Dialogs for each tool
        when (selectedTool) {
            "REPORT" -> EmotionalReportDialog(diaries) { selectedTool = null }
            "FIND_DATE" -> FindDiaryByDateDialog(diaries) { selectedTool = null }
            "MUYU" -> ZenWoodenFishDialog { selectedTool = null }
            "BREATHE" -> BreathingGuideDialog { selectedTool = null }
            "NOISE" -> NatureWhiteNoiseDialog { selectedTool = null }
            "BOOK" -> BookOfAnswersDialog { selectedTool = null }
            "THERMO" -> StressThermometerDialog { selectedTool = null }
            "CARDS" -> MindfulnessCardsDialog { selectedTool = null }
        }
    }
}

data class InteractiveToolItem(
    val id: String,
    val title: String,
    val desc: String,
    val icon: String,
    val bgColor: Color
)

// ==================== 1. 年/月度回顾报告 ====================
@Composable
fun EmotionalReportDialog(diaries: List<DiaryEntry>, onDismiss: () -> Unit) {
    var reportRange by remember { mutableStateOf("MONTH") } // MONTH or YEAR
    val currentCalendar = Calendar.getInstance()
    val thisYear = currentCalendar.get(Calendar.YEAR)
    val thisMonth = currentCalendar.get(Calendar.MONTH) + 1 // 1-indexed

    val filteredDiaries = remember(diaries, reportRange) {
        diaries.filter { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.date }
            val entryYear = cal.get(Calendar.YEAR)
            val entryMonth = cal.get(Calendar.MONTH) + 1
            if (reportRange == "MONTH") {
                entryYear == thisYear && entryMonth == thisMonth
            } else {
                entryYear == thisYear
            }
        }
    }

    val stats = remember(filteredDiaries) {
        if (filteredDiaries.isEmpty()) {
            null
        } else {
            val total = filteredDiaries.size
            val avgScore = filteredDiaries.map { it.moodValue }.average().toFloat()
            val moodCounts = filteredDiaries.groupingBy { it.mood }.eachCount()

            // Find dominant mood
            val dominantMood = moodCounts.maxByOrNull { it.value }?.key ?: "CALM"

            val happinessRatio = (filteredDiaries.count { it.mood == "HAPPY" || it.mood == "CALM" }.toFloat() / total * 100).toInt()

            val suggestion = when {
                avgScore >= 4.0 -> "近期你的心灵状态充满了阳光与喜悦！就像一个硕果累累的小橘林，温暖且满足。请继续保持对生活的细微观察，常怀感恩，你正在创造极其美好的生活频率喔 🌟"
                avgScore >= 3.0f -> "近期情绪温和安稳，身心处于舒适的平衡期。日常如微风拂过绿叶，虽无大喜大悲，但拥有充足的内在底气。建议保持良好的作息，偶然尝试一些未体验过的新鲜小事，能让能量更充沛 🍃"
                avgScore >= 2.0f -> "近期有些焦虑和压力哦，偶尔也会陷入失落。你可能承担了太多对未来的期许，或者是身体在呼唤休息。别担心，小树仔给你一个厚厚的虚拟拥抱！请分一些精力来照顾当下的身体，喝杯热茶，允许自己先'停一下' 🧸"
                else -> "近期可能处于严重的情绪低潮或疲累期。这并非你的错，每个人都有阴雨连绵的日子。泥土里的种子也需要黑暗才能发芽。现在最需要的是自我关怀，停止自责，适度倾听舒缓音乐，如果需要可以向亲人或专业人士寻求爱与支持 ☔"
            }

            ReportStats(total, avgScore, dominantMood, happinessRatio, suggestion, moodCounts)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🗓️ 心灵回顾总结报告",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                // Range Selector Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    listOf(
                        Pair("MONTH", "${thisMonth}月度回顾"),
                        Pair("YEAR", "${thisYear}年度回顾")
                    ).forEach { (key, label) ->
                        val isSelected = reportRange == key
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                )
                                .clickable { reportRange = key }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Report Content
                if (stats == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌱", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "在这个时间段您还没有记录日记喔！\n去写几篇日记再来查看吧~",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Stat Blocks Row
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("记账篇数 📝", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${stats.totalEntries} 篇", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("平均情绪指数 🌡️", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${String.format("%.1f", stats.avgScore)} 分", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("绿码喜悦率 🌈", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${stats.happinessRatio}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        // Mood Breakdown
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "🎨 情绪分布频率",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                val moods = listOf(
                                    Triple("HAPPY", "快乐开心 🥰", Color(0xFFE07A5F)),
                                    Triple("CALM", "宁静祥和 🍃", Color(0xFF8C9C75)),
                                    Triple("SAD", "沮丧忧伤 ☔", Color(0xFF2E5B88)),
                                    Triple("ANXIOUS", "紧张焦虑 🌪️", Color(0xFFF59E0B)),
                                    Triple("ANGRY", "生气暴躁 ⚡", Color(0xFFEF4444))
                                )

                                moods.forEach { (key, label, color) ->
                                    val count = stats.moodCounts[key] ?: 0
                                    val percentage = if (stats.totalEntries > 0) (count.toFloat() / stats.totalEntries * 100).toInt() else 0

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.width(90.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(8.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(if (percentage > 0) percentage / 100f else 0.01f)
                                                    .clip(CircleShape)
                                                    .background(color)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "$count 篇 ($percentage%)",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            modifier = Modifier.width(60.dp),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                }
                            }
                        }

                        // AI Emotional Summary Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "🌲 小树仔的温情心灵解读",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stats.suggestion,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("收下这份回顾")
                }
            }
        }
    }
}

data class ReportStats(
    val totalEntries: Int,
    val avgScore: Float,
    val dominantMood: String,
    val happinessRatio: Int,
    val suggestion: String,
    val moodCounts: Map<String, Int>
)

// ==================== 2. 查看某年某月某日的日记 ====================
@Composable
fun FindDiaryByDateDialog(diaries: List<DiaryEntry>, onDismiss: () -> Unit) {
    var yearStr by remember { mutableStateOf("2026") }
    var monthStr by remember { mutableStateOf("7") }
    var dayStr by remember { mutableStateOf("18") }

    val filteredDiaries = remember(diaries, yearStr, monthStr, dayStr) {
        val y = yearStr.toIntOrNull() ?: 2026
        val m = monthStr.toIntOrNull() ?: 7
        val d = dayStr.toIntOrNull() ?: 18

        diaries.filter { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.date }
            cal.get(Calendar.YEAR) == y &&
                    (cal.get(Calendar.MONTH) + 1) == m &&
                    cal.get(Calendar.DAY_OF_MONTH) == d
        }
    }

    var selectedDiaryForDetail by remember { mutableStateOf<DiaryEntry?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔍 检索特定日子手账",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Text(
                    text = "在下方选择或输入您想要回顾的日期：",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                // Simple date selectors
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Year Select
                    OutlinedTextField(
                        value = yearStr,
                        onValueChange = { yearStr = it },
                        label = { Text("年", fontSize = 10.sp) },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    // Month Select
                    OutlinedTextField(
                        value = monthStr,
                        onValueChange = { monthStr = it },
                        label = { Text("月", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    // Day Select
                    OutlinedTextField(
                        value = dayStr,
                        onValueChange = { dayStr = it },
                        label = { Text("日", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Query results
                Text(
                    text = "查询结果 (${filteredDiaries.size} 篇)：",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                if (filteredDiaries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("☁️", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "那一天你似乎没有写日记呀……\n尝试调整一下上面的日期吧~",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredDiaries) { diary ->
                            val moodEmoji = when (diary.mood) {
                                "HAPPY" -> "🥰 快乐"
                                "CALM" -> "🍃 平静"
                                "SAD" -> "☔ 沮丧"
                                "ANXIOUS" -> "🌪️ 焦虑"
                                "ANGRY" -> "⚡ 生气"
                                else -> "🧸 治愈"
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDiaryForDetail = diary },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = diary.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = diary.content,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = moodEmoji,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("返回小工具")
                }
            }
        }

        // Detailed view dialog inside FindDiaryByDateDialog
        selectedDiaryForDetail?.let { diary ->
            // Simple Read Only dialog for specific diary inside tools
            Dialog(onDismissRequest = { selectedDiaryForDetail = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .padding(12.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📖 翻阅往日手账",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = { selectedDiaryForDetail = null }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                            }
                        }

                        val formattedTime = remember(diary.date) {
                            val d = Date(diary.date)
                            DateFormat.format("yyyy年MM月dd日 HH:mm", d).toString()
                        }

                        Text(
                            text = formattedTime,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = diary.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = diary.content,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 21.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (diary.aiCommentary.isNotBlank()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "🌲 小树仔的寄语：",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = diary.aiCommentary,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { selectedDiaryForDetail = null },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("返回结果列表")
                        }
                    }
                }
            }
        }
    }
}


// ==================== 3. 电子静心木鱼 ====================
@Composable
fun ZenWoodenFishDialog(onDismiss: () -> Unit) {
    var clickCount by remember { mutableStateOf(0) }
    var scale by remember { mutableStateOf(1f) }
    val scope = rememberCoroutineScope()

    // Floating text list for tap effect
    val floatingTexts = remember { mutableStateListOf<FloatingText>() }

    val floatingWords = listOf(
        "功德 +1",
        "烦恼 -1",
        "平静 +1",
        "压力 -1",
        "好运 +1",
        "福气 +1",
        "快乐 +1"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)) // Wooden/Zen dark color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🐟 电子静心木鱼",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE2E8F0)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = Color(0xFFE2E8F0)
                        )
                    }
                }

                Text(
                    text = "闭目养神，轻敲木鱼。\n静心笃定，功德无量。",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                // Stats Counter
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("当前功德:", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                        Text("$clickCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                }

                // Interactive Tap Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Floating Animations container
                    floatingTexts.forEach { fText ->
                        FloatingTextItem(fText) {
                            floatingTexts.remove(fText)
                        }
                    }

                    // Wooden Fish Graphic
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF5C4033), Color(0xFF321E15))
                                )
                            )
                            .border(2.dp, Color(0xFF8B5A2B), CircleShape)
                            .clickable {
                                clickCount++
                                scale = 0.85f
                                scope.launch {
                                    delay(80)
                                    scale = 1.05f
                                    delay(50)
                                    scale = 1.0f
                                }
                                // Create floating text
                                val word = floatingWords.random()
                                floatingTexts.add(
                                    FloatingText(
                                        id = System.nanoTime(),
                                        text = word,
                                        offsetX = (-30..30).random().toFloat(),
                                        offsetY = -20f
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🐟", fontSize = 54.sp)
                            Text(
                                text = "TAP ME",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE2E8F0).copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                Button(
                    onClick = { clickCount = 0 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("功德圆满/清零计次", color = Color.White)
                }
            }
        }
    }
}

data class FloatingText(
    val id: Long,
    val text: String,
    val offsetX: Float,
    val offsetY: Float
)

@Composable
fun FloatingTextItem(fText: FloatingText, onFinished: () -> Unit) {
    val transitionState = remember { MutableTransitionState(false).apply { targetState = true } }
    val transition = rememberTransition(transitionState, label = "floatingText")

    val alpha by transition.animateFloat(
        transitionSpec = { tween(800) },
        label = "alpha"
    ) { state ->
        if (state) 0f else 1.0f
    }

    val translateUp by transition.animateFloat(
        transitionSpec = { tween(800) },
        label = "translate"
    ) { state ->
        if (state) -120f else 0f
    }

    LaunchedEffect(transitionState.currentState) {
        if (!transitionState.targetState && !transitionState.currentState) {
            onFinished()
        } else if (transitionState.currentState) {
            // Once it reaches final state, toggle targetState false to fade out
            transitionState.targetState = false
        }
    }

    Box(
        modifier = Modifier
            .offset(x = fText.offsetX.dp, y = (fText.offsetY + translateUp).dp)
            .graphicsLayer(alpha = alpha)
    ) {
        Text(
            text = fText.text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            textAlign = TextAlign.Center
        )
    }
}


// ==================== 4. 深呼吸引导舱 ====================
@Composable
fun BreathingGuideDialog(onDismiss: () -> Unit) {
    var phase by remember { mutableStateOf("READY") } // READY, INHALE, HOLD, EXHALE
    var secondsLeft by remember { mutableStateOf(4) }
    var scaleAnim = remember { Animatable(1.0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(phase, secondsLeft) {
        if (phase != "READY") {
            if (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            } else {
                when (phase) {
                    "INHALE" -> {
                        phase = "HOLD"
                        secondsLeft = 4
                    }
                    "HOLD" -> {
                        phase = "EXHALE"
                        secondsLeft = 4
                    }
                    "EXHALE" -> {
                        phase = "INHALE"
                        secondsLeft = 4
                    }
                }
            }
        }
    }

    // Dynamic animation scale based on breathe stage
    LaunchedEffect(phase) {
        when (phase) {
            "INHALE" -> {
                scaleAnim.animateTo(
                    targetValue = 1.6f,
                    animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
                )
            }
            "EXHALE" -> {
                scaleAnim.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
                )
            }
            "HOLD" -> {
                // Stay scaled
            }
            else -> {
                scaleAnim.snapTo(1.0f)
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧘 正念呼吸引导舱",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Text(
                    text = "采用4-4-4正念呼吸大法，缓缓吸气、气沉丹田，能瞬间安抚你的中枢副交感神经，排遣紧迫感喔。",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                // Animated breathing container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Ripple
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .scale(scaleAnim.value * 1.15f)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                CircleShape
                            )
                    )

                    // Middle Bubble
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .scale(scaleAnim.value)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (phase) {
                                    "INHALE" -> "💨 吸气"
                                    "HOLD" -> "🔒 憋气"
                                    "EXHALE" -> "🌬️ 呼气"
                                    else -> "🧘 Ready"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            if (phase != "READY") {
                                Text(
                                    text = "$secondsLeft 秒",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // Control Button
                if (phase == "READY") {
                    Button(
                        onClick = {
                            phase = "INHALE"
                            secondsLeft = 4
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("开启静心呼吸循环 (4分钟)")
                    }
                } else {
                    Button(
                        onClick = {
                            phase = "READY"
                            secondsLeft = 4
                            scope.launch { scaleAnim.snapTo(1f) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("退出静心引导")
                    }
                }
            }
        }
    }
}


// ==================== 5. 疗愈白噪音伴侣 ====================
@Composable
fun NatureWhiteNoiseDialog(onDismiss: () -> Unit) {
    // We simulate 5 nature sound mixers
    var playingSound by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌧️ 疗愈白噪音伴侣",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Text(
                    text = "选择大自然的天然声波，将大脑频率调整至平和舒缓的 Alpha 状态。伴随舒缓的声波，进入深层专注或香甜睡眠吧。",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                // Currently playing banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (playingSound == null) {
                            Text("⏸️ 伴侣静音中，点下方卡片开启混音", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        } else {
                            Text("🎶 正在播放：$playingSound", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            // Animated equalizer lines
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                val infiniteTransition = rememberInfiniteTransition(label = "eq")
                                repeat(4) { index ->
                                    val heightFactor by infiniteTransition.animateFloat(
                                        initialValue = 4f,
                                        targetValue = 16f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(durationMillis = 300 + (index * 100), easing = FastOutSlowInEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "eq_height"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(width = 3.dp, height = heightFactor.dp)
                                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(1.dp))
                                    )
                                }
                            }
                        }
                    }
                }

                // Noise Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sounds = listOf(
                        Triple("RAIN", "深夜江南暴雨 🌧️", "噼里啪啦、雨落芭蕉的白噪音"),
                        Triple("FIRE", "温暖林间篝火 🔥", "柴火毕剥、温暖碎裂的炉火声"),
                        Triple("SEA", "深海幽蓝潮汐 🌊", "海洋波浪、潮涨潮落的温柔拍击"),
                        Triple("WIND", "林梢沙沙微风 🍃", "清晨原野微风吹拂竹林的安谧"),
                        Triple("NIGHT", "夏夜萤火虫鸣 🌌", "乡野夏夜、虫鸣蛙叫的乡愁曲")
                    )

                    sounds.forEach { (id, title, desc) ->
                        val isPlaying = playingSound == title
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    playingSound = if (isPlaying) null else title
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isPlaying) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.background
                            ),
                            border = BorderStroke(
                                width = if (isPlaying) 1.5.dp else 0.5.dp,
                                color = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                    contentDescription = "播放控制",
                                    tint = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("继续享受白噪音")
                }
            }
        }
    }
}


// ==================== 6. 治愈答案之书 ====================
@Composable
fun BookOfAnswersDialog(onDismiss: () -> Unit) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf<String?>(null) }
    var isShaking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val answerPool = listOf(
        "顺其自然，当下即是最好的答案。🌸",
        "睡个好觉，明天自然有解法。🛋️",
        "不妨大胆一点，去追求你内心深处的真正直觉吧。✨",
        "先吃一顿热气腾腾的美食，答案就会浮出水面。🍲",
        "有些事情，暂时放手，反而会迎来转机。🎈",
        "坚信自己，小树洞的满天繁星都在默默祝福你。🌟",
        "今天可能不是最好的起点，但它是最棒的积蓄期。🍃",
        "不要往回看，你已经做得好极了。🌲",
        "一切焦灼都源于想得太快，先放慢脚步呼吸。🧘",
        "这个烦恼在三个月后，其实微不足道喔。🧸",
        "勇敢拒绝，你的善良值得留给对的人。💖",
        "保持微笑，最奇妙的惊喜正在你意想不到的街角等你。🐱"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖 治愈心灵答案之书",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                Text(
                    text = "闭上眼睛，在心里默念你目前最纠结、最焦虑的一个烦恼/决定5秒钟。然后轻轻叩开答案之书，接受小树仔赐予你的神圣治愈指示吧。",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                // Input field
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    placeholder = { Text("（可选）在这里写下你的困惑，增强心灵感应...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 12.sp)
                )

                // The Magic Book Visual Area
                val shakeAngle = remember { Animatable(0f) }
                LaunchedEffect(isShaking) {
                    if (isShaking) {
                        repeat(5) {
                            shakeAngle.animateTo(12f, tween(50))
                            shakeAngle.animateTo(-12f, tween(50))
                        }
                        shakeAngle.animateTo(0f, tween(50))
                        isShaking = false
                        answer = answerPool.random()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (answer == null) {
                        // Glowing book cover
                        Box(
                            modifier = Modifier
                                .size(width = 110.dp, height = 135.dp)
                                .graphicsLayer(rotationZ = shakeAngle.value)
                                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFFFFD700),
                                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp)
                                )
                                .clickable {
                                    isShaking = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📖", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("答案之书", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("点击开启", fontSize = 8.sp, color = Color(0xFFFFD700))
                            }
                        }
                    } else {
                        // Revealed parchment card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBF7)),
                            border = BorderStroke(1.dp, Color(0xFFE6D2B1)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("📜 契机答案已现：", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC084FC))
                                Text(
                                    text = answer ?: "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A3525),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                                Text(
                                    text = "—— 无论前路如何，请好好爱自己 ——",
                                    fontSize = 9.sp,
                                    color = Color(0xFF9E8E7A),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (answer != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                answer = null
                                question = ""
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("重新默念提问")
                        }
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("铭记于心")
                        }
                    }
                }
            }
        }
    }
}


// ==================== 7. 压力测试温度计 ====================
@Composable
fun StressThermometerDialog(onDismiss: () -> Unit) {
    var step by remember { mutableStateOf(1) } // Steps 1 to 4, then 5 (result)
    var q1 by remember { mutableStateOf(3f) } // Sliders/Values from 1 to 5
    var q2 by remember { mutableStateOf(3f) }
    var q3 by remember { mutableStateOf(3f) }
    var q4 by remember { mutableStateOf(3f) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌡️ 情绪压力测试仪",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                if (step in 1..4) {
                    // Quiz progress
                    LinearProgressIndicator(
                        progress = step / 4f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                    )

                    // Current Question text
                    val questionTitle = when (step) {
                        1 -> "1. 最近一周里，你的【入睡/睡眠质量】如何？💤"
                        2 -> "2. 处理日常工作/学习事务时，【拖延、烦躁】的频率是？🕒"
                        3 -> "3. 是否经常觉得胸闷、肩膀酸痛，难以获得彻底放松？🧱"
                        else -> "4. 面对突发小阻碍，你产生暴躁、自责或悲观的心态是？⚡"
                    }

                    val questionDesc = when (step) {
                        1 -> "1分：夜夜香甜无梦到天明；5分：翻来覆去难以入眠、多恶梦。"
                        2 -> "1分：干劲十足、效率贼高；5分：严重拖延、抗拒社交工作。"
                        3 -> "1分：浑身轻松舒坦；5分：时刻感到肌肉紧绷、提不起精神。"
                        else -> "1分：平静面对、微笑化解；5分：陷入严重内耗，极易情绪崩溃。"
                    }

                    var currentValue by remember(step) {
                        mutableStateOf(
                            when (step) {
                                1 -> q1
                                2 -> q2
                                3 -> q3
                                else -> q4
                            }
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = questionTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = questionDesc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom Slider scale
                        Slider(
                            value = currentValue,
                            onValueChange = {
                                currentValue = it
                                when (step) {
                                    1 -> q1 = it
                                    2 -> q2 = it
                                    3 -> q3 = it
                                    else -> q4 = it
                                }
                            },
                            valueRange = 1f..5f,
                            steps = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("非常轻松 (1)", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            Text("中度亚健康 (3)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            Text("重度焦虑 (5)", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (step > 1) {
                            OutlinedButton(
                                onClick = { step-- },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("上一题")
                            }
                        }
                        Button(
                            onClick = { step++ },
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (step == 4) "生成测试报告" else "下一题")
                        }
                    }
                } else {
                    // Result Section
                    val totalScore = q1 + q2 + q3 + q4
                    val stressPercent = ((totalScore - 4) / 16f * 100).toInt()

                    val resultTitle = when {
                        totalScore <= 8 -> "状态极佳！身心防线满格 🟢"
                        totalScore <= 14 -> "轻中度紧张！需要多加关注 🟡"
                        else -> "高度紧绷！红色预警信号 🔴"
                    }

                    val resultTip = when {
                        totalScore <= 8 -> "你目前的心灵状态就像刚洗干净晒满太阳的棉被，蓬松而温暖！你的情绪免疫力非常高，继续以平常心对待一切挑战。今天可以去吃一顿大餐或者跑个步来庆祝喔！✨"
                        totalScore <= 14 -> "你最近扛了过多的责任，属于典型的轻度亚健康紧绷状态。小树洞建议：每天划定15分钟完全关掉电子设备。闭上眼，在白噪音的陪伴下让大脑彻底放空，偶尔去公园'抱抱树'吧 🍃"
                        else -> "警报响起！你可能正在经历高强度的精神消耗。你的内心小池塘已经被压力泥沙淤积。现在最明智的做法是：停下手头的所有非必要工作。今晚十点上床深度睡眠，向信任的亲友倾诉。小树仔会作为你的守护神守护着你，切勿继续硬撑 🧸"
                    }

                    val thermoColor = when {
                        totalScore <= 8 -> Color(0xFF10B981)
                        totalScore <= 14 -> Color(0xFFF59E0B)
                        else -> Color(0xFFEF4444)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Visual Thermometer
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // Thermometer bulb progress
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(if (stressPercent > 0) stressPercent / 100f else 0.05f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(thermoColor)
                            )
                            Text(
                                text = "心智负荷温度：$stressPercent℃",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (stressPercent > 50) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }

                        Text(
                            text = resultTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = thermoColor
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "🩹 小树仔的自愈处方：",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = resultTip,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Button(
                            onClick = {
                                step = 1
                                q1 = 3f
                                q2 = 3f
                                q3 = 3f
                                q4 = 3f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("重新评测")
                        }
                    }
                }
            }
        }
    }
}


// ==================== 8. 正念每日灵感卡 ====================
@Composable
fun MindfulnessCardsDialog(onDismiss: () -> Unit) {
    var cardIndex by remember { mutableStateOf(0) }
    var cardColorTheme by remember { mutableStateOf(0) } // 0: Sage, 1: Rose, 2: Deep Blue, 3: Golden Sunset

    val cards = listOf(
        MindfulnessCard(
            text = "我们常常花太多精力去规划完美的未来，却忘记了当下正在呼吸的这一秒，才是生命真实的唯一舞台。放轻松，深呼吸，你正安稳地被大自然拥抱着。🍃",
            author = "《正念安之》"
        ),
        MindfulnessCard(
            text = "情绪如潮水般涌来，也终会如潮水般退去。你不需要去堵住悲伤，不需要去掐灭焦虑。允许它们经过你，就像云朵经过青松，而你依旧是那棵沉稳的青松。🌲",
            author = "《森林物语》"
        ),
        MindfulnessCard(
            text = "有时候，最强大的力量不是坚持，而是释怀。原谅那些让你失望的，放手那些你无能为力的。给自己的心，腾出一个洒满阳光的空白角落。🌞",
            author = "《断舍离寄语》"
        ),
        MindfulnessCard(
            text = "请对自己温柔一点。你只是一个凡人，会累，会哭，会犯错。这不是软弱，而是生命的温度。今晚洗个香香的热水澡，允许自己做一个不完美的、被爱的人。🧸",
            author = "《树洞树仔的温暖信》"
        ),
        MindfulnessCard(
            text = "每一个日子都值得庆祝，哪怕它极其普通。哪怕只是买到了一朵好看的玫瑰，或是吃到一碗好吃的面。生活，不就是在微小的欢愉中拼凑出的宇宙吗？✨",
            author = "《小确幸指南》"
        )
    )

    val currentCard = cards[cardIndex]

    val backgroundBrush = when (cardColorTheme) {
        1 -> Brush.verticalGradient(listOf(Color(0xFFFFF0F3), Color(0xFFFFC2D1)))
        2 -> Brush.verticalGradient(listOf(Color(0xFFEDF4FC), Color(0xFFBACADB)))
        3 -> Brush.verticalGradient(listOf(Color(0xFFFFF9E6), Color(0xFFFFD1A9)))
        else -> Brush.verticalGradient(listOf(Color(0xFFF3F7F0), Color(0xFFC7D3C1))) // Sage default
    }

    val textColor = when (cardColorTheme) {
        1 -> Color(0xFF5E1E2F)
        2 -> Color(0xFF1E2E3E)
        3 -> Color(0xFF5A3E15)
        else -> Color(0xFF2C3A23)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✨ 正念每日灵感卡",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                    }
                }

                // Aesthetic card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(backgroundBrush)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "“",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.3f),
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Text(
                            text = currentCard.text,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = "”",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.3f),
                            modifier = Modifier.align(Alignment.End)
                        )

                        Text(
                            text = "—— ${currentCard.author}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Palette buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            Color(0xFFC7D3C1), // Sage
                            Color(0xFFFFC2D1), // Rose
                            Color(0xFFBACADB), // Blue
                            Color(0xFFFFD1A9)  // Sunset
                        ).forEachIndexed { idx, color ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (cardColorTheme == idx) 2.dp else 0.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                    .clickable { cardColorTheme = idx }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            cardIndex = (cardIndex + 1) % cards.size
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("换一张灵感")
                    }
                }
            }
        }
    }
}

data class MindfulnessCard(
    val text: String,
    val author: String
)
