package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = "user_default",
    val name: String = "Alex Rivera",
    val email: String = "alex@mybrand.com",
    val planTier: String = "FREE",
    val isLoggedIn: Boolean = true,
    val authToken: String = "sess_token_mock_local",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: String = "Marketing",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_content")
data class SavedContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val contentType: String,
    val content: String,
    val originalPrompt: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val platform: String = "",
    val tags: String = "",
    val projectId: Long = 0
)

@Entity(tableName = "brand_voice")
data class BrandVoiceEntity(
    @PrimaryKey
    val id: Int = 1,
    val brandName: String = "LuxeGlow Skincare",
    val description: String = "Organic, clinically backed hydration products for busy professionals.",
    val targetAudience: String = "Urban professionals aged 25-45 who value clean beauty.",
    val tone: String = "LUXURY",
    val wordsToUse: String = "radiance, effortless, clinically tested, ritual, glow",
    val wordsToAvoid: String = "cheap, magical, overnight miracle, harsh chemicals",
    val personality: String = "Sophisticated, warm, science-backed, premium",
    val isActive: Boolean = true
)

@Entity(tableName = "usage_tracking")
data class UsageEntity(
    @PrimaryKey
    val id: Int = 1,
    val dateString: String = "",
    val generationsToday: Int = 0,
    val lastResetTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey
    val id: Int = 1,
    val planTier: String = "FREE",
    val billingCycle: String = "monthly",
    val startDate: Long = System.currentTimeMillis(),
    val renewalDate: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
    val isActive: Boolean = true
)
