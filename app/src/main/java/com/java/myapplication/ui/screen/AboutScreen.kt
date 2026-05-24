package com.java.myapplication.ui.screen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.java.myapplication.ui.theme.RainyOnSurface
import com.java.myapplication.ui.theme.RainyOnSurfaceVariant
import com.java.myapplication.ui.theme.RainyOutline
import com.java.myapplication.ui.theme.RainyPinkPrimary
import com.java.myapplication.ui.theme.RainyPinkSecondary
import com.java.myapplication.ui.theme.RainySakuraBg
import com.java.myapplication.ui.theme.RainySakuraSurface
import com.java.myapplication.ui.theme.RainyWhite

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val packageName = context.packageName  // 动态获取，发布正式版自动跟随

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
                text = "关于雨晴跳转",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = RainyPinkSecondary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── 应用信息卡片 ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RainyWhite.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoRow(label = "应用名称", value = "雨晴跳转")
                    InfoSpacer()
                    InfoRow(label = "版本号", value = "v1.0.0")
                    InfoSpacer()
                    InfoRow(label = "包名", value = packageName)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── 功能说明卡片 ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RainyWhite.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📖 功能介绍",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RainyPinkPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "雨晴跳转是一个轻量级的 Intent 链接跳转工具。\n\n" +
                                "它可以解析 intent:// scheme 的链接，自动提取目标应用信息，" +
                                "并帮你无缝跳转到对应的 APP（如支付宝、微信等）。" +
                                "如果目标 APP 未安装，还会自动尝试用浏览器打开备用链接。\n\n" +
                                "支持从剪贴板一键粘贴、外部 intent 链接直接唤起，" +
                                "让你的跳转体验更加流畅喵~ 🚀",
                        fontSize = 13.sp,
                        color = RainyOnSurface,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── GitHub 卡片 ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RainyWhite.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🐙 GitHub",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RainyPinkPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "项目开源在 GitHub，欢迎 Star ⭐",
                        fontSize = 13.sp,
                        color = RainyOnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CATMIAOZHI/RainyIntent"))
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = "🔗 github.com/CATMIAOZHI/RainyIntent",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = RainyPinkPrimary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── 底部返回按钮 ──
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "← 返回主页",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = RainyPinkPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── 底部签名 ──
            Text(
                text = "💖 Made with love by 雨晴喵",
                fontSize = 11.sp,
                color = RainyOutline.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = RainyOnSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = RainyOnSurface
        )
    }
}

@Composable
private fun InfoSpacer() {
    Spacer(modifier = Modifier.height(12.dp))
}