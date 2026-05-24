package com.java.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.java.myapplication.ui.theme.MyApplicationTheme
import com.java.myapplication.ui.theme.RainyOnSurface
import com.java.myapplication.ui.theme.RainyOnSurfaceVariant
import com.java.myapplication.ui.theme.RainyOutline
import com.java.myapplication.ui.theme.RainyPinkPrimary
import com.java.myapplication.ui.theme.RainyPinkSecondary
import com.java.myapplication.ui.theme.RainySakuraBg
import com.java.myapplication.ui.theme.RainySakuraSurface
import com.java.myapplication.ui.theme.RainyWhite
import com.java.myapplication.ui.screen.AboutScreen
import java.net.URLDecoder

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "IntentBridge"
    }

    // 类级别状态，Compose 可观察
    private var inputText by mutableStateOf("")
    private var statusText by mutableStateOf("")
    private var showAbout by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                if (showAbout) {
                    AboutScreen(
                        onBackClick = { showAbout = false }
                    )
                } else {
                    MainUI(
                        inputText = inputText,
                        statusText = statusText,
                        onInputChange = { inputText = it },
                        onOpenClick = { parseAndDispatch(inputText) },
                        onPasteClick = { pasteFromClipboard() },
                        onAboutClick = { showAbout = true }
                    )
                }
            }
        }

        // 处理从外部 intent:// scheme 传入的链接 → 自动填入
        handleIncomingIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    // 从剪贴板粘贴
    private fun pasteFromClipboard() {
        try {
            val cm = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
            val clip = cm?.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val pasted = clip.getItemAt(0).text?.toString() ?: ""
                if (pasted.isNotBlank()) {
                    inputText = pasted
                    statusText = "已粘贴喵~ 点击「打开链接」即可跳转 ✨"
                } else {
                    statusText = "剪贴板是空的呢…"
                }
            } else {
                statusText = "剪贴板没有内容喵~"
            }
        } catch (e: Exception) {
            statusText = "读取剪贴板失败: ${e.message}"
        }
    }

    // ─── 核心：接收外部 intent:// URI ────────────────────
    private fun handleIncomingIntent(intent: Intent) {
        val uri = intent.data
        if (uri != null && uri.scheme == "intent") {
            Log.i(TAG, "收到外部 intent:// URI: $uri")
            inputText = uri.toString()
            statusText = "已收到链接，正在跳转喵~ 🚀"
            parseAndDispatch(uri.toString())
        }
    }

    // ─── 解析 Intent URI 并转发到目标 APP ─────────────────
    private fun parseAndDispatch(rawUri: String) {
        if (rawUri.isBlank()) {
            statusText = "请先输入 intent 链接喵~"
            return
        }
        try {
            val trimmed = rawUri.trim()
            val hashIndex = trimmed.indexOf("#Intent;")
            if (hashIndex == -1) {
                statusText = "无效的 Intent URI：缺少 #Intent; 标记"
                return
            }

            val basePart = trimmed.substring(0, hashIndex)
            val fragmentPart = trimmed.substring(hashIndex + "#Intent;".length)

            val params = parseIntentFragment(fragmentPart)

            val targetScheme = params["scheme"] ?: "https"
            val targetPackage = params["package"]
            val fallbackUrl = params["S.browser_fallback_url"]
                ?: params["browser_fallback_url"]

            val targetUriStr = basePart.replaceFirst("intent://", "$targetScheme://")

            Log.i(TAG, "目标 scheme: $targetScheme")
            Log.i(TAG, "目标 package: $targetPackage")
            Log.i(TAG, "目标 URI: $targetUriStr")
            Log.i(TAG, "Fallback URL: $fallbackUrl")

            dispatchToTarget(targetUriStr, targetPackage, fallbackUrl)

        } catch (e: Exception) {
            Log.e(TAG, "解析失败", e)
            statusText = "解析失败: ${e.message}"
        }
    }

    // ─── 解析 #Intent fragment 中的键值对 ─────────────────
    private fun parseIntentFragment(fragment: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val cleaned = fragment
            .removeSuffix(";end")
            .removeSuffix(";end;")

        val parts = cleaned.split(";")
        for (part in parts) {
            val eqIndex = part.indexOf("=")
            if (eqIndex > 0) {
                val key = part.substring(0, eqIndex)
                var value = part.substring(eqIndex + 1)
                try {
                    value = URLDecoder.decode(value, "UTF-8")
                } catch (_: Exception) { }
                result[key] = value
            }
        }
        return result
    }

    // ─── 启动目标 Intent ──────────────────────────────────
    private fun dispatchToTarget(
        targetUriStr: String,
        targetPackage: String?,
        fallbackUrl: String?
    ) {
        try {
            val targetUri = Uri.parse(targetUriStr)
            val targetIntent = Intent(Intent.ACTION_VIEW, targetUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (!targetPackage.isNullOrBlank()) {
                    setPackage(targetPackage)
                }
            }

            val resolved = targetIntent.resolveActivity(packageManager)
            if (resolved != null) {
                startActivity(targetIntent)
                statusText = "已跳转到: $targetPackage ✅"
                Toast.makeText(this, "跳转成功喵~ 🎉", Toast.LENGTH_SHORT).show()
            } else {
                throw ActivityNotFoundException("未找到能处理 $targetUriStr 的应用")
            }

        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "目标应用未安装", e)
            if (!fallbackUrl.isNullOrBlank()) {
                try {
                    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(fallbackIntent)
                    statusText = "目标APP未安装，已用浏览器打开 🌐"
                } catch (e2: Exception) {
                    statusText = "无法打开链接，请确认已安装目标APP"
                }
            } else {
                statusText = "目标APP未安装: ${targetPackage ?: "未知"}"
            }
        } catch (e: SecurityException) {
            statusText = "权限不足：无法启动目标应用"
        }
        // 不再自动关闭APP，用户可以继续使用
    }
}

// ─── 主界面 ────────────────────────────────────────────
@Composable
fun MainUI(
    inputText: String,
    statusText: String,
    onInputChange: (String) -> Unit,
    onOpenClick: () -> Unit,
    onPasteClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(RainySakuraBg, RainySakuraSurface),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ── 猫耳装饰 ──
            Text(text = "🐱", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(4.dp))

            // ── 标题 ──
            Text(
                text = "雨晴跳转",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = RainyPinkSecondary,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(2.dp))

            // ── 副标题 ──
            Text(
                text = "把 intent 链接粘贴进来，帮你跳转到对应APP喵~",
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = RainyOnSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── 输入框卡片 ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RainyWhite.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 多行输入框
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        placeholder = {
                            Text(
                                text = "在此粘贴 intent:// 链接...\n例如支付宝支付链接、微信跳转链接等",
                                fontSize = 13.sp,
                                color = RainyOutline.copy(alpha = 0.6f),
                                lineHeight = 18.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RainyPinkPrimary,
                            unfocusedBorderColor = RainyPinkPrimary.copy(alpha = 0.3f),
                            cursorColor = RainyPinkPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.sp,
                            color = RainyOnSurface,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 按钮行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 粘贴按钮
                        OutlinedButton(
                            onClick = onPasteClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = RainyPinkPrimary
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(listOf(RainyPinkPrimary, RainyPinkSecondary))
                            )
                        ) {
                            Text("📋 一键粘贴", fontWeight = FontWeight.Medium)
                        }

                        // 打开按钮
                        Button(
                            onClick = onOpenClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RainyPinkPrimary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text("🚀 打开链接", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── 状态显示区 ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (statusText.isNotBlank()) RainyWhite.copy(alpha = 0.85f) else Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                if (statusText.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 小圆点指示器
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(RainyPinkPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusText,
                            fontSize = 13.sp,
                            color = RainyOnSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── 底部区域：签名 + 关于入口 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔗 Powered by 雨晴喵",
                    fontSize = 11.sp,
                    color = RainyOutline.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
                TextButton(
                    onClick = onAboutClick,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "关于 ℹ️",
                        fontSize = 12.sp,
                        color = RainyPinkPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}