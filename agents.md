# 🤖 Agent Configuration & Project Context

This document serves as the persistent memory and instruction set for the AI Coding Assistant (Antigravity) working on the Mihon OCR Fork.

## 🎯 Current Objective
Maintain and extend a custom Mihon fork that enables an offline "Mokuro" experience for reading manhua on Android.

## 🛠️ Tech Stack
- **Languages**: Kotlin, Jetpack Compose.
- **Framework**: Mihon (Tachiyomi fork).
- **AI/ML**: Google ML Kit (Text Recognition & Translation).
- **Background**: Android WorkManager.
- **Database**: SQLDelight (CC-CEDICT).

## 🧬 Key Components
- `eu.kanade.tachiyomi.data.ocr.OCRProcessor`: Core OCR logic.
- `eu.kanade.tachiyomi.data.ocr.OCRWorker`: Background task manager.
- `eu.kanade.tachiyomi.ui.reader.viewer.OCROverlayView`: UI rendering layer.
- `eu.kanade.tachiyomi.data.ocr.TranslationProcessor`: Offline translator.
- `eu.kanade.tachiyomi.data.dictionary.CEDICTManager`: Dictionary installer.

## 📜 Coding Guidelines
1. **Performance First**: The user is on a Fold 7. Use hardware acceleration and NPU-optimized models.
2. **Offline-First**: Never rely on external APIs. All models must be downloadable and runnable locally.
3. **Mihon Architecture**: Respect Mihon's `Injekt` dependency injection and `ReaderViewModel` state management patterns.
4. **Clean UI**: Use premium aesthetics (semi-transparent overlays, smooth animations).

## 🚀 Active Task
- **Completed**: OCR background trigger, Translation popup, Overlay rendering, CC-CEDICT local dictionary integration, AnkiDroid integration (One-tap).
- **Next**: Customizable bubble highlights and visual themes.

## 🔗 References
- [Mihon Repository](https://github.com/mihonapp/mihon)
- [ML Kit Documentation](https://developers.google.com/ml-kit)
- [Mokuro Reader](https://github.com/kha-white/mokuro) (Inspiration)
