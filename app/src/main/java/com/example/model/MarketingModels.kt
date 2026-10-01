package com.example.model

enum class ContentType(val displayName: String, val category: String) {
    INSTAGRAM_CAPTION("Instagram Caption", "Social"),
    FACEBOOK_POST("Facebook Post", "Social"),
    TIKTOK_SCRIPT("TikTok Script", "Video"),
    PRODUCT_DESCRIPTION("Product Description", "E-Commerce"),
    GOOGLE_AD("Google Ad", "Advertising"),
    FACEBOOK_AD("Facebook Ad", "Advertising"),
    EMAIL("Email", "Marketing"),
    WHATSAPP_MESSAGE("WhatsApp Message", "Messaging"),
    LINKEDIN_POST("LinkedIn Post", "Professional"),
    MARKETING_IDEA("Marketing Idea", "Strategy")
}

enum class Tone(val displayName: String) {
    PROFESSIONAL("Professional"),
    FRIENDLY("Friendly"),
    LUXURY("Luxury"),
    FUNNY("Funny"),
    PERSUASIVE("Persuasive"),
    URGENT("Urgent"),
    MINIMAL("Minimal"),
    ENERGETIC("Energetic")
}

enum class Language(val displayName: String, val code: String) {
    ENGLISH("English", "en"),
    FRENCH("French", "fr"),
    ARABIC("Arabic", "ar"),
    SPANISH("Spanish", "es")
}

enum class ContentLength(val displayName: String) {
    SHORT("Short"),
    MEDIUM("Medium"),
    LONG("Long")
}

enum class AdPlatform(val displayName: String) {
    FACEBOOK_ADS("Facebook Ads"),
    INSTAGRAM_ADS("Instagram Ads"),
    GOOGLE_ADS("Google Ads"),
    TIKTOK_ADS("TikTok Ads")
}

enum class PlanTier(val displayName: String, val maxGenerationsPerDay: Int) {
    FREE("Free Plan", 5),
    PRO("Pro Unlimited", Int.MAX_VALUE)
}

data class AdVariation(
    val id: Int,
    val headline: String,
    val primaryText: String,
    val description: String,
    val cta: String
)

data class ProductDescriptionResult(
    val shortDescription: String,
    val fullDescription: String,
    val keyBenefits: List<String>,
    val features: List<String>,
    val cta: String
)

data class TikTokScriptResult(
    val hook: String,
    val scriptBeats: List<String>,
    val cta: String,
    val caption: String,
    val hashtags: List<String>
)

data class InstagramResult(
    val hook: String,
    val caption: String,
    val cta: String,
    val hashtags: List<String>
)

data class EmailResult(
    val subject: String,
    val subjectAlt: String,
    val previewText: String,
    val body: String,
    val cta: String
)

data class HashtagsResult(
    val highReach: List<String>,
    val mediumCompetition: List<String>,
    val niche: List<String>
)

data class BrandVoice(
    val brandName: String = "",
    val description: String = "",
    val targetAudience: String = "",
    val tone: Tone = Tone.PERSUASIVE,
    val wordsToUse: String = "",
    val wordsToAvoid: String = "",
    val personality: String = "Authoritative yet approachable",
    val isActive: Boolean = true
)

data class UserAccount(
    val name: String = "Alex Rivera",
    val email: String = "alex@mybrand.com",
    val planTier: PlanTier = PlanTier.FREE,
    val generationsToday: Int = 2,
    val isLoggedIn: Boolean = true
)

data class CalendarDay(
    val dayNumber: Int,
    val dayName: String,
    val platform: String,
    val postTheme: String,
    val contentSummary: String,
    val bestTimeToPost: String
)

data class FullCampaign(
    val productName: String,
    val strategy: String,
    val customerProfile: String,
    val coreMarketingAngle: String,
    val mainOffer: String,
    val usp: String,
    val hooks: List<String>,
    val instagramPosts: List<String>,
    val tiktokConcepts: List<String>,
    val metaAds: List<String>,
    val googleAds: List<String>,
    val emails: List<String>,
    val whatsappMessage: String,
    val hashtags: String,
    val contentCalendar: List<CalendarDay>
) {
    fun getSectionContent(type: CampaignSectionType): String = when (type) {
        CampaignSectionType.STRATEGY -> strategy
        CampaignSectionType.CUSTOMER_PROFILE -> customerProfile
        CampaignSectionType.CORE_ANGLE -> coreMarketingAngle
        CampaignSectionType.MAIN_OFFER -> mainOffer
        CampaignSectionType.USP -> usp
        CampaignSectionType.HOOKS -> hooks.joinToString("\n\n")
        CampaignSectionType.INSTAGRAM_POSTS -> instagramPosts.joinToString("\n\n---\n\n")
        CampaignSectionType.TIKTOK_CONCEPTS -> tiktokConcepts.joinToString("\n\n---\n\n")
        CampaignSectionType.META_ADS -> metaAds.joinToString("\n\n---\n\n")
        CampaignSectionType.GOOGLE_ADS -> googleAds.joinToString("\n\n---\n\n")
        CampaignSectionType.EMAILS -> emails.joinToString("\n\n---\n\n")
        CampaignSectionType.WHATSAPP -> whatsappMessage
        CampaignSectionType.HASHTAGS -> hashtags
        CampaignSectionType.CONTENT_CALENDAR -> contentCalendar.joinToString("\n\n") { day ->
            "📅 ${day.dayName} [${day.platform}] - Post Time: ${day.bestTimeToPost}\nTheme: ${day.postTheme}\nPlan: ${day.contentSummary}"
        }
    }

    fun withUpdatedSection(type: CampaignSectionType, newContent: String): FullCampaign = when (type) {
        CampaignSectionType.STRATEGY -> copy(strategy = newContent)
        CampaignSectionType.CUSTOMER_PROFILE -> copy(customerProfile = newContent)
        CampaignSectionType.CORE_ANGLE -> copy(coreMarketingAngle = newContent)
        CampaignSectionType.MAIN_OFFER -> copy(mainOffer = newContent)
        CampaignSectionType.USP -> copy(usp = newContent)
        CampaignSectionType.HOOKS -> copy(hooks = newContent.split("\n\n").filter { it.isNotBlank() })
        CampaignSectionType.INSTAGRAM_POSTS -> copy(instagramPosts = newContent.split("\n\n---\n\n").filter { it.isNotBlank() })
        CampaignSectionType.TIKTOK_CONCEPTS -> copy(tiktokConcepts = newContent.split("\n\n---\n\n").filter { it.isNotBlank() })
        CampaignSectionType.META_ADS -> copy(metaAds = newContent.split("\n\n---\n\n").filter { it.isNotBlank() })
        CampaignSectionType.GOOGLE_ADS -> copy(googleAds = newContent.split("\n\n---\n\n").filter { it.isNotBlank() })
        CampaignSectionType.EMAILS -> copy(emails = newContent.split("\n\n---\n\n").filter { it.isNotBlank() })
        CampaignSectionType.WHATSAPP -> copy(whatsappMessage = newContent)
        CampaignSectionType.HASHTAGS -> copy(hashtags = newContent)
        CampaignSectionType.CONTENT_CALENDAR -> this
    }

    fun toExportMarkdown(): String = buildString {
        appendLine("# 🚀 COMPLETE MARKETING CAMPAIGN: $productName")
        appendLine("Generated by SellAI • Turn ideas into content that sells\n")
        appendLine("## 1. CAMPAIGN STRATEGY")
        appendLine(strategy)
        appendLine("\n---\n## 2. TARGET CUSTOMER PROFILE")
        appendLine(customerProfile)
        appendLine("\n---\n## 3. CORE MARKETING ANGLE")
        appendLine(coreMarketingAngle)
        appendLine("\n---\n## 4. MAIN OFFER")
        appendLine(mainOffer)
        appendLine("\n---\n## 5. UNIQUE SELLING PROPOSITION (USP)")
        appendLine(usp)
        appendLine("\n---\n## 6. 3 CAMPAIGN HOOKS")
        hooks.forEachIndexed { i, hook -> appendLine("\n### Hook ${i + 1}\n$hook") }
        appendLine("\n---\n## 7. 3 INSTAGRAM POSTS")
        instagramPosts.forEachIndexed { i, post -> appendLine("\n### Instagram Post ${i + 1}\n$post") }
        appendLine("\n---\n## 8. 3 TIKTOK CONCEPTS & SCRIPTS")
        tiktokConcepts.forEachIndexed { i, concept -> appendLine("\n### TikTok Concept ${i + 1}\n$concept") }
        appendLine("\n---\n## 9. 3 FACEBOOK / INSTAGRAM ADS")
        metaAds.forEachIndexed { i, ad -> appendLine("\n### Meta Ad Variation ${i + 1}\n$ad") }
        appendLine("\n---\n## 10. 3 GOOGLE SEARCH ADS")
        googleAds.forEachIndexed { i, ad -> appendLine("\n### Google Ad Variation ${i + 1}\n$ad") }
        appendLine("\n---\n## 11. 3 MARKETING EMAILS")
        emails.forEachIndexed { i, email -> appendLine("\n### Email ${i + 1}\n$email") }
        appendLine("\n---\n## 12. WHATSAPP PROMOTIONAL MESSAGE")
        appendLine(whatsappMessage)
        appendLine("\n---\n## 13. STRATEGIC HASHTAG SETS")
        appendLine(hashtags)
        appendLine("\n---\n## 14. 7-DAY CONTENT CALENDAR")
        contentCalendar.forEach { day ->
            appendLine("• ${day.dayName} (${day.platform}) [${day.bestTimeToPost}]: ${day.postTheme} - ${day.contentSummary}")
        }
    }
}

enum class CampaignSectionType(val title: String, val platformTag: String) {
    STRATEGY("Campaign Strategy", "Strategy"),
    CUSTOMER_PROFILE("Target Customer Profile", "Audience"),
    CORE_ANGLE("Core Marketing Angle", "Angle"),
    MAIN_OFFER("Main Offer", "Offer"),
    USP("Unique Selling Proposition", "USP"),
    HOOKS("3 Campaign Hooks", "Social"),
    INSTAGRAM_POSTS("3 Instagram Posts", "Instagram"),
    TIKTOK_CONCEPTS("3 TikTok Concepts & Scripts", "TikTok"),
    META_ADS("3 Facebook/Instagram Ads", "Meta Ads"),
    GOOGLE_ADS("3 Google Ad Variations", "Google Ads"),
    EMAILS("3 Marketing Emails", "Email"),
    WHATSAPP("WhatsApp Promotional Message", "WhatsApp"),
    HASHTAGS("Hashtag Sets", "Hashtags"),
    CONTENT_CALENDAR("7-Day Content Calendar", "Calendar")
}

enum class ThemeMode(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light Mode"),
    DARK("Dark Mode")
}

enum class Screen(val title: String) {
    SPLASH("Splash"),
    ONBOARDING("Onboarding"),
    LOGIN("Sign In"),
    REGISTER("Create Account"),
    FORGOT_PASSWORD("Reset Password"),
    DASHBOARD("Home"),
    CREATE_HUB("Create"),
    CAMPAIGN_GENERATOR("Campaigns"),
    SAVED_CONTENT("Saved Library"),
    PROFILE("Profile"),
    AI_WRITER("AI Writer"),
    PRODUCT_DESCRIPTION("Product Description"),
    SOCIAL_MEDIA("Social Media"),
    ADS("Ads"),
    EMAIL("Email"),
    HASHTAGS("Hashtags"),
    BRAND_VOICE("Brand Voice"),
    SETTINGS("Settings"),
    LANDING("Landing Page")
}
