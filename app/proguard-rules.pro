# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# SellAI Production R8 / ProGuard Configuration

# Line numbers in crash reports
-keepattributes SourceFile,LineNumberTable

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class com.example.data.local.** { *; }

# Moshi & Models
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}
-keep class com.example.model.** { *; }

# OkHttp & Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Kotlin Coroutines
-keepclassmembernames class kotlinx.coroutines.internal.MainDispatcherFactory {
    kotlinx.coroutines.MainCoroutineDispatcher createDispatcher(java.util.List);
}
-keepclassmembernames class kotlinx.coroutines.CoroutineExceptionHandler {
    void handleException(kotlin.coroutines.CoroutineContext, java.lang.Throwable);
}
