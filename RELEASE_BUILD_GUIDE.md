# SellAI Google Play Release Build Guide

This document describes how to configure your production signing credentials and build an Android App Bundle (`.aab`) ready for Google Play upload.

---

## 1. Application Details

- **App Name**: SellAI
- **Package Name / Application ID**: `com.sellai.app`
- **Target SDK**: 36 (Android 16, fully compliant with modern Google Play policy)
- **Min SDK**: 24 (Android 7.0+, covers 99%+ of global active Android devices)
- **Version Code**: 1
- **Version Name**: 1.0.0

---

## 2. Generating Your Production Signing Keystore

Do not commit private keystores or passwords to git. Create a secure upload key:

```bash
keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
```

Keep your keystore file (`my-upload-key.jks`) safe and backed up.

---

## 3. Configuring the Signing Credentials

In `app/build.gradle.kts`, the release signing block is pre-configured to read credentials from environment variables:

```kotlin
signingConfigs {
    create("release") {
        val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
        storeFile = file(keystorePath)
        storePassword = System.getenv("STORE_PASSWORD") ?: ""
        keyAlias = "upload"
        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
    }
}
```

Export your credentials in your terminal or CI/CD environment (e.g. GitHub Actions, Google Cloud Build):

```bash
export KEYSTORE_PATH="/absolute/path/to/my-upload-key.jks"
export STORE_PASSWORD="your_secure_keystore_password"
export KEY_PASSWORD="your_secure_key_password"
```

---

## 4. Building the Android App Bundle (.aab)

Run the Gradle release task:

```bash
gradle :app:bundleRelease
```

The resulting bundle file will be generated at:
`app/build/outputs/bundle/release/app-release.aab`

You can upload this `.aab` file directly to the **Google Play Console** under **Internal Testing**, **Closed Testing**, or **Production**.

---

## 5. Security & Gemini AI Credentials

- **Client-Side Security**: The Gemini API key is never hardcoded into Java/Kotlin source code.
- **Environment Ingestion**: The key is injected securely via `Secrets Gradle Plugin` from `.env` or CI/CD secrets.
- **Backend Architecture**: `GeminiClient` supports routing through a dedicated backend API service (`BASE_URL`) or server-side Cloud Function/Cloud Run when deployed in commercial SaaS production.
- **Offline / Zero-Key Fallback**: In offline environments or if no key is configured, SellAI gracefully utilizes its built-in deterministic marketing engine (`MarketingEngine`) without crashing or showing broken interfaces.

---

## 6. ProGuard & R8 Optimization

Code shrinking and obfuscation are enabled in `app/build.gradle.kts` via `proguard-rules.pro`. Keep rules are configured for Room entities, Moshi serializers, OkHttp, and Jetpack Compose.
