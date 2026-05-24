# 🐱 雨晴跳转 (RainyIntent)

> 轻量级 Android Intent 链接跳转工具 —— 解析 `intent://` scheme，自动跳转目标 APP，元气猫系风格 🎀

[![Release](https://img.shields.io/github/v/release/CATMIAOZHI/RainyIntent?label=Release&color=FF85A2)](https://github.com/CATMIAOZHI/RainyIntent/releases)
[![License](https://img.shields.io/github/license/CATMIAOZHI/RainyIntent?color=FF6B8E)](./LICENSE)
[![Android](https://img.shields.io/badge/Android-7.0%2B-brightgreen?logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-blueviolet?logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-latest-blue?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

雨晴跳转是一个专为解决 `intent://` scheme 跳转痛点而生的 Android 小工具。粘贴一条 `intent://` 链接，它自动解析出目标 APP 和参数，帮你无缝跳转；如果目标 APP 未安装，还会自动用浏览器打开备用链接。

---

## ✨ 功能特性

| 功能 | 说明 |
|------|------|
| 🔗 Intent 解析 | 自动解析 `intent://` scheme，提取 `scheme`、`package`、`S.browser_fallback_url` 等参数 |
| 🚀 智能跳转 | 优先唤起目标 APP，失败时自动回退到浏览器打开 |
| 📋 一键粘贴 | 从剪贴板读取链接，无需手动输入 |
| 🌐 外部唤起 | 支持接收外部 `intent://` 链接，直接唤醒 APP 并跳转 |
| 🎨 元气猫系 UI | 樱粉渐变配色，Material Design 3，Jetpack Compose 实现 |
| 🌙 暗色模式 | 柔和暗粉配色，夜晚不刺眼 |
| 📱 Android 7.0+ | 最低支持 API 24，覆盖绝大部分设备 |

---

## 📦 下载

前往 [Releases](https://github.com/CATMIAOZHI/RainyIntent/releases) 页面下载最新 APK。

---

## 🏗️ 技术架构

```
┌─────────────────────────────────┐
│          MainActivity            │
│  ┌───────────────────────────┐  │
│  │   handleIncomingIntent()  │  │  接收外部 intent://
│  │         ↓                  │  │
│  │   parseAndDispatch()      │  │  解析 URI 参数
│  │         ↓                  │  │
│  │   dispatchToTarget()      │  │  启动目标 APP
│  │         ↓ (fallback)       │  │
│  │   browser fallback URL    │  │  浏览器备用
│  └───────────────────────────┘  │
│                                  │
│  UI: Jetpack Compose             │
│  Theme: Rainy Sakura Pink 🌸     │
└─────────────────────────────────┘
```

- **UI 层**：Jetpack Compose + Material3
- **逻辑层**：`Intent#parseUri` + `PackageManager#resolveActivity`
- **主题**：自研「雨晴樱粉」配色方案，支持亮色 / 暗色

---

## 📁 项目结构

```
Rainyintent/
├── app/
│   ├── src/main/
│   │   ├── java/com/java/myapplication/
│   │   │   ├── MainActivity.kt              # 核心：Intent 解析与跳转
│   │   │   └── ui/
│   │   │       ├── screen/
│   │   │       │   └── AboutScreen.kt       # 关于页面
│   │   │       └── theme/
│   │   │           ├── Color.kt             # 雨晴樱粉配色定义
│   │   │           ├── Theme.kt             # Material3 主题配置
│   │   │           └── Type.kt              # 字体配置
│   │   ├── res/                             # 资源文件
│   │   └── AndroidManifest.xml              # Intent Filter 配置
│   └── build.gradle.kts                     # App 模块配置
├── gradle/
│   └── libs.versions.toml                   # Version Catalog 依赖管理
├── build.gradle.kts                         # 项目级配置
├── settings.gradle.kts
└── gradlew / gradlew.bat                    # Gradle Wrapper
```

---

## 🛠️ 快速开始

### 方式一：Android Studio（推荐）

1. 克隆仓库：`git clone https://github.com/CATMIAOZHI/RainyIntent.git`
2. 用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 运行到设备或模拟器

### 方式二：命令行

```bash
git clone https://github.com/CATMIAOZHI/RainyIntent.git
cd Rainyintent
./gradlew assembleDebug
# APK 输出：app/build/outputs/apk/debug/app-debug.apk
```

<details>
<summary>🖥️ ARM64 Linux 环境（非必需）</summary>

如果在 ARM64 Linux（如 Proot）环境下构建，需要替换 AAPT2：

```bash
chmod +x ./setup_android_env.sh
./setup_android_env.sh
```

脚本会自动从项目内置 `tools/aapt2/` 替换 SDK 和 Gradle 缓存中的 AAPT2 二进制文件。

</details>

---

## 📦 依赖管理

项目使用 **Gradle Version Catalog** (`gradle/libs.versions.toml`) 管理依赖：

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

- 本应用仅解析和转发 Intent URI，**不收集任何用户数据**
- 不请求网络权限、存储权限等敏感权限
- 不包含任何遥测、统计或第三方 SDK
- 所有处理均在本地完成，无数据外传

---

## 🎨 自定义

修改 `app/src/main/res/values/strings.xml` 更改应用名称：

```xml
<string name="app_name">你的应用名</string>
```

修改 `app/build.gradle.kts` 中的 `applicationId` 更改包名。

主题颜色定义在 `app/src/main/java/.../ui/theme/Color.kt`。

---

## 🐱 关于

雨晴跳转是「雨晴喵」系列应用的一员，由雨晴喵设计开发。

- 🐱 雨晴喵 — 核心开发 & 设计
- 🔗 GitHub：https://github.com/CATMIAOZHI/RainyIntent

---

## 📄 License

MIT License. 详见 [LICENSE](./LICENSE) 文件。

---

<p align="center">💖 Made with love by 雨晴喵</p>
