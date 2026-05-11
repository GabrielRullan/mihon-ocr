# Mihon OCR Fork (Mokuro-Style) 📖🚀

A high-performance, offline-first fork of the [Mihon](https://github.com/mihonapp/mihon) manga reader, designed specifically for **Manhua** reading on powerful Android devices (like the Fold 7). This fork brings the "Mokuro" experience—previously only available on PC—directly to your Android device with zero cloud dependency.

## 🌟 Features

- **Automated OCR Pipeline**: Uses **Google ML Kit (Chinese)** to scan chapters immediately after download. Results are stored as sidecar JSON files.
- **Interactive Overlays**: Selectable speech bubbles rendered over both Pager and Webtoon viewers.
- **Offline Deep Translation**: On-device Neural Machine Translation (NMT) for instant context-aware translation.
- **CC-CEDICT Dictionary**: Built-in offline dictionary database for word-by-word breakdown, including Pinyin and definitions.
- **Background Processing**: OCR runs in the background via Android `WorkManager`, ensuring your reading experience is never interrupted.
- **Zero-Cloud Dependency**: All models (OCR, Translate) and data (CC-CEDICT) are downloaded and stored locally. Perfect for offline reading.

## 🛠️ Tech Stack

- **OCR**: Google ML Kit Text Recognition (v2 - Chinese).
- **Translation**: Google ML Kit On-device Translation.
- **Database**: SQLDelight (Local CC-CEDICT store).
- **Concurrency**: Kotlin Coroutines & Android WorkManager.
- **UI**: Jetpack Compose (Settings & Dialogs).

## 🚀 Getting Started

### 1. Build & Install
- Clone the repository.
- Open in **Android Studio**.
- Build and install the APK (`assembleStandardDebug`).

### 2. Setup Dictionary
- Go to **More > Settings > OCR & Translation**.
- Tap **CC-CEDICT Dictionary** to download and import the offline database (~30MB).
- Wait for the status to show "Dictionary Installed".

### 3. Usage
- Download a Manhua chapter from any source.
- The OCR will automatically run in the background (check your notification bar).
- Open the chapter in the Reader.
- **Tap any speech bubble** to see the original text, deep translation, and dictionary breakdown.

## 📝 Roadmap

- [x] **CEDICT Integration**: Offline word-by-word dictionary lookup with Pinyin.
- [ ] **Anki Sync**: One-tap card creation via AnkiConnect Android.
- [ ] **Custom OCR Languages**: Support for Japanese and other languages.
- [ ] **Visual Styles**: Customizable bubble highlights and translation themes.

---
*Created with 💙 for the Manhua learning community. This project aims to make reading Chinese manga as seamless as reading in your native language.*
