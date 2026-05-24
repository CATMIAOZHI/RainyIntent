# 🌧️ 雨晴跳转 (RainyIntent)

> *"喵~ 把 intent 链接交给雨晴，帮你一键跳转！"* 🐱✨

一个**轻量级的 Android Intent 链接跳转工具**，专治各种 `intent://` scheme 链接无法直接打开的痛点。不拦截、不劫持、纯转发。

[![Release](https://github.com/CATMIAOZHI/RainyIntent/actions/workflows/release.yml/badge.svg)](https://github.com/CATMIAOZHI/RainyIntent/actions)
[![Version](https://img.shields.io/github/v/release/CATMIAOZHI/RainyIntent?color=ff85a2&style=flat-square)](https://github.com/CATMIAOZHI/RainyIntent/releases/latest)

---

## ✨ 功能特性

| 功能 | 说明 |
|------|------|
| 🔗 **Intent 解析** | 自动解析 `intent://` scheme，提取 scheme / package / fallback URL 等参数 |
| 🚀 **智能跳转** | 优先唤起目标 APP，失败时自动回退到浏览器打开备用链接 |
| 📋 **一键粘贴** | 从剪贴板读取链接，无需手动输入 |
| 🌐 **外部唤起** | 支持接收外部 `intent://` 链接，直接唤醒 APP 并跳转 |
| 🎨 **元气猫系 UI** | 樱粉渐变配色，Material Design 3，Jetpack Compose 实现 |
| 🌙 **暗色模式** | 柔和暗粉配色，夜晚不刺眼 |
| 📱 **Android 7.0+** | 最低支持 API 24，覆盖绝大部分设备 |
| 🔒 **零数据收集** | 不联网、不传数据、不请求敏感权限 |

---

## 📦 下载

| 方式 | 链接 |
|------|------|
| 📥 **最新版 APK** | [Releases 页面](https://github.com/CATMIAOZHI/RainyIntent/releases/latest) |
| 📋 **所有版本** | [Releases](https://github.com/CATMIAOZHI/RainyIntent/releases) |

---

## 🏗️ 技术架构

```
┌──────────────────────────────────────┐
│           MainActivity               │
│  ┌────────────────────────────────┐  │
│  │   handleIncomingIntent()       │  │  接收外部 intent://
│  │         ↓                       │  │
│  │   parseAndDispatch()           │  │  解析 URI 参数
│  │         ↓                       │  │
│  │   dispatchToTarget()           │  │  启动目标 APP
│  │         ↓ (fallback)            │  │
│  │   browser fallback URL         │  │  浏览器备用
│  ├────────────────────────────────┤  │
│  │   Jetpack Compose UI           │  │
│  │   雨晴樱粉主题 (亮色/暗色)      │  │
│  └────────────────────────────────┘  │
│        PackageManager + Intent        │
└──────────────────────────────────────┘
```

### 技术栈
- **语言**：Kotlin 100%
- **UI 框架**：Jetpack Compose + Material Design 3
- **核心逻辑**：Android Intent 解析 + PackageManager
- **构建**：Gradle + AGP 9.0

---

## 📁 项目结构

```
RainyIntent/
├── .github/workflows/
│   └── release.yml                        # GitHub Actions 自动构建 + 发布
├── app/
│   ├── src/main/
│   │   ├── java/com/java/myapplication/
│   │   │   ├── MainActivity.kt            # 核心：Intent 解析与跳转
│   │   │   └── ui/
│   │   │       ├── screen/
│   │   │       │   └── AboutScreen.kt     # 关于页面
│   │   │       └── theme/
│   │   │           ├── Color.kt           # 雨晴樱粉配色
│   │   │           ├── Theme.kt           # Material3 主题
│   │   │           └── Type.kt            # 字体配置
│   │   ├── res/                           # 图标、字符串等资源
│   │   └── AndroidManifest.xml            # Intent Filter 配置
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                 # Version Catalog
├── build.gradle.kts
└── gradlew / gradlew.bat
```

---

## 🛠️ 构建

**环境**：JDK 17+、Android SDK (compileSdk 35)

```bash
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK（已配置签名）
./gradlew installDebug       # 安装到设备
```

APK 输出：`app/build/outputs/apk/`

<details>
<summary>🖥️ ARM64 Linux 环境（非必需）</summary>

在 ARM64 Linux（如 Proot）下构建需替换 AAPT2：

```bash
chmod +x ./setup_android_env.sh
./setup_android_env.sh
```

</details>

---

## 📦 依赖管理

项目使用 **Gradle Version Catalog** (`gradle/libs.versions.toml`)：

| 依赖 | 版本 |
|------|------|
| AGP | 9.0.0 |
| Kotlin | 2.3.10 |
| Compose BOM | 2026.01.01 |
| Material3 | (via BOM) |
| compileSdk | 35 |
| minSdk | 24 |

---

## 🔒 安全说明

- 仅解析和转发 Intent URI，**不收集任何用户数据**
- 不请求网络权限、存储权限等敏感权限
- 不包含任何遥测、统计或第三方 SDK
- 所有处理均在本地完成

---

## 🐱 关于

雨晴跳转是「雨晴喵」系列应用的一员，由雨晴喵设计开发。

🔗 https://github.com/CATMIAOZHI/RainyIntent

---

## 📄 许可

MIT License

---

<p align="center">💖 Made with love by 雨晴喵</p>
