package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getChatResponse(
        history: List<ChatMessage>,
        customApiKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = if (!customApiKey.isNullOrBlank()) customApiKey else BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "小树仔检测到您还没有配置 API Key 哦！请到“设置”页配置专属 API Key 激活完全体的我，或者直接调侃我的基础本地回复模组喔~"
        }

        try {
            val contentsArray = JSONArray()

            val systemPrompt = "你是一个温暖、贴心、善解人意的情绪树洞AI助手（叫做“小树仔”）。你的任务是倾听用户的快乐与烦恼，提供温暖的情感引导、共情和治愈性的建议。字数不宜过多，保持在100字左右，语气柔和。多使用语气词和温馨的表达。"

            // Build historical messages
            for (msg in history.takeLast(15)) {
                val contentObj = JSONObject()
                val roleStr = if (msg.sender == "USER") "user" else "model"
                contentObj.put("role", roleStr)
                
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", msg.message)
                partsArray.put(partObj)
                
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
            }

            val requestBodyJson = JSONObject()
            requestBodyJson.put("contents", contentsArray)

            // System instruction
            val sysInstructionObj = JSONObject()
            val sysPartsArray = JSONArray()
            val sysPartObj = JSONObject()
            sysPartObj.put("text", systemPrompt)
            sysPartsArray.put(sysPartObj)
            sysInstructionObj.put("parts", sysPartsArray)
            requestBodyJson.put("systemInstruction", sysInstructionObj)

            val requestBodyStr = requestBodyJson.toString()
            Log.d(TAG, "Request payload: $requestBodyStr")

            val url = "$BASE_URL?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBodyStr.toRequestBody(mediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "Response failed: ${response.code} - $errBody")
                    return@withContext "唔……树洞好像有点塞车（错误码: ${response.code}），让小树仔默默陪着你就好喔。"
                }

                val responseBodyStr = response.body?.string() ?: ""
                Log.d(TAG, "Response payload: $responseBodyStr")

                val jsonResponse = JSONObject(responseBodyStr)
                val candidatesArray = jsonResponse.getJSONArray("candidates")
                if (candidatesArray.length() > 0) {
                    val firstCandidate = candidatesArray.getJSONObject(0)
                    val contentObj = firstCandidate.getJSONObject("content")
                    val partsArray = contentObj.getJSONArray("parts")
                    if (partsArray.length() > 0) {
                        return@withContext partsArray.getJSONObject(0).getString("text")
                    }
                }
                "唔……我的叶子没有捕捉到声音，能再说一遍吗？"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getChatResponse", e)
            "树洞的网络飘到外太空去啦：${e.localizedMessage}"
        }
    }

    suspend fun analyzeDiary(
        content: String,
        customApiKey: String? = null
    ): DiaryAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = if (!customApiKey.isNullOrBlank()) customApiKey else BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getLocalAnalysisResult(content)
        }

        try {
            val systemPrompt = """
                你是一个资深心理咨询师和温馨情绪分析师。请分析用户写的日记内容，并返回对应的JSON格式。
                
                你必须且只能返回一个合法的JSON字符串，不要包含 ```json 或 ``` 等 Markdown 标记，格式如下：
                {
                  "mood": "HAPPY", 
                  "score": 4.5,
                  "advice": "这真是一段美妙的时光，小树仔为你感到高兴！"
                }
                
                参数说明：
                1. "mood" 只能是以下5个大写字符串之一：
                   - "HAPPY" (用户感到高兴、兴奋、满足等积极情绪)
                   - "CALM" (用户感到平静、轻松、安宁)
                   - "SAD" (用户感到悲伤、失落、孤独等)
                   - "ANXIOUS" (用户感到焦虑、紧张、迷茫、压力大)
                   - "ANGRY" (用户感到愤怒、烦躁、不满)
                2. "score" 是一个浮点数，范围为 1.0 到 5.0：
                   - 5.0 代表情绪状态极好，1.0 代表情绪非常低落/极端负面。
                   - HAPPY 通常在 4.0 - 5.0 之间
                   - CALM 通常在 3.0 - 4.0 之间
                   - SAD/ANXIOUS/ANGRY 通常在 1.0 - 3.0 之间
                3. "advice" 是 1 到 2 句非常温柔、贴心、鼓励或共情的评语（不超过50字）。
            """.trimIndent()

            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            contentObj.put("role", "user")
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", "日记内容：\n$content")
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)

            val requestBodyJson = JSONObject()
            requestBodyJson.put("contents", contentsArray)

            // System instruction
            val sysInstructionObj = JSONObject()
            val sysPartsArray = JSONArray()
            val sysPartObj = JSONObject()
            sysPartObj.put("text", systemPrompt)
            sysPartsArray.put(sysPartObj)
            sysInstructionObj.put("parts", sysPartsArray)
            requestBodyJson.put("systemInstruction", sysInstructionObj)

            val configObj = JSONObject()
            configObj.put("responseMimeType", "application/json")
            requestBodyJson.put("generationConfig", configObj)

            val requestBodyStr = requestBodyJson.toString()
            val url = "$BASE_URL?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBodyStr.toRequestBody(mediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext getLocalAnalysisResult(content)
                }

                val responseBodyStr = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseBodyStr)
                val candidatesArray = jsonResponse.getJSONArray("candidates")
                if (candidatesArray.length() > 0) {
                    val firstCandidate = candidatesArray.getJSONObject(0)
                    val contentObj2 = firstCandidate.getJSONObject("content")
                    val partsArray2 = contentObj2.getJSONArray("parts")
                    if (partsArray2.length() > 0) {
                        val rawText = partsArray2.getJSONObject(0).getString("text").trim()
                        val resultJson = JSONObject(rawText)
                        val mood = resultJson.optString("mood", "CALM").uppercase()
                        val score = resultJson.optDouble("score", 3.0).toFloat()
                        val advice = resultJson.optString("advice", "倾听你的心声，小树仔一直在这里。")
                        return@withContext DiaryAnalysisResult(mood, score, advice)
                    }
                }
                getLocalAnalysisResult(content)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in analyzeDiary", e)
            getLocalAnalysisResult(content)
        }
    }

    private fun getLocalAnalysisResult(content: String): DiaryAnalysisResult {
        val trimmed = content.trim()
        return when {
            trimmed.contains("开心") || trimmed.contains("棒") || trimmed.contains("喜") || trimmed.contains("快乐") || trimmed.contains("高") || trimmed.contains("爽") || trimmed.contains("甜") -> 
                DiaryAnalysisResult("HAPPY", 4.8f, "听到这个好消息，小树仔真为你感到开心！把快乐记录下来吧！")
            trimmed.contains("难过") || trimmed.contains("悲伤") || trimmed.contains("哭") || trimmed.contains("委屈") || trimmed.contains("痛") || trimmed.contains("惨") -> 
                DiaryAnalysisResult("SAD", 1.8f, "呜呜，给你一个大大的拥抱。想哭就哭出来吧，小树仔会一直陪着你。")
            trimmed.contains("气") || trimmed.contains("烦") || trimmed.contains("怒") || trimmed.contains("讨厌") || trimmed.contains("崩溃") || trimmed.contains("火大") -> 
                DiaryAnalysisResult("ANGRY", 2.2f, "深呼吸，呼—— 吸—— 烦恼都吹跑！小树仔在这里当你的出气筒。")
            trimmed.contains("焦虑") || trimmed.contains("慌") || trimmed.contains("紧") || trimmed.contains("怕") || trimmed.contains("压力") || trimmed.contains("累") || trimmed.contains("忙") -> 
                DiaryAnalysisResult("ANXIOUS", 2.5f, "别太紧绷啦，你已经做得很棒了。一步一步来，明天会更好的！")
            else -> 
                DiaryAnalysisResult("CALM", 3.5f, "岁月静好，平静也是一种美好。愿你的生活每天都安宁祥和。")
        }
    }
}

data class DiaryAnalysisResult(
    val mood: String, // HAPPY, CALM, SAD, ANXIOUS, ANGRY
    val score: Float, // 1.0 to 5.0
    val advice: String
)
