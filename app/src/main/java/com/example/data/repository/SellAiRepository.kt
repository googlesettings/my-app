package com.example.data.repository

import com.example.data.local.BrandVoiceDao
import com.example.data.local.BrandVoiceEntity
import com.example.data.local.ProjectDao
import com.example.data.local.ProjectEntity
import com.example.data.local.SavedContentDao
import com.example.data.local.SavedContentEntity
import com.example.data.local.SubscriptionDao
import com.example.data.local.SubscriptionEntity
import com.example.data.local.UsageDao
import com.example.data.local.UsageEntity
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.data.remote.GeminiClient
import com.example.data.remote.MarketingEngine
import com.example.model.AdPlatform
import com.example.model.AdVariation
import com.example.model.BrandVoice
import com.example.model.CampaignSectionType
import com.example.model.ContentLength
import com.example.model.ContentType
import com.example.model.EmailResult
import com.example.model.FullCampaign
import com.example.model.HashtagsResult
import com.example.model.InstagramResult
import com.example.model.Language
import com.example.model.PlanTier
import com.example.model.ProductDescriptionResult
import com.example.model.TikTokScriptResult
import com.example.model.Tone
import com.example.model.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class SellAiRepository(
    private val userDao: UserDao,
    private val projectDao: ProjectDao,
    private val savedContentDao: SavedContentDao,
    private val brandVoiceDao: BrandVoiceDao,
    private val usageDao: UsageDao,
    private val subscriptionDao: SubscriptionDao
) {
    val allSavedContent: Flow<List<SavedContentEntity>> = savedContentDao.getAllSavedContent()
    val favoriteSavedContent: Flow<List<SavedContentEntity>> = savedContentDao.getFavorites()
    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    val currentBrandVoice: Flow<BrandVoice> = brandVoiceDao.getBrandVoice().map { entity ->
        if (entity != null) {
            val toneEnum = try {
                Tone.valueOf(entity.tone)
            } catch (e: Exception) {
                Tone.PERSUASIVE
            }
            BrandVoice(
                brandName = entity.brandName,
                description = entity.description,
                targetAudience = entity.targetAudience,
                tone = toneEnum,
                wordsToUse = entity.wordsToUse,
                wordsToAvoid = entity.wordsToAvoid,
                personality = entity.personality,
                isActive = entity.isActive
            )
        } else {
            BrandVoice(
                brandName = "LuxeGlow Skincare",
                description = "Organic, clinically backed hydration products for modern lifestyles.",
                targetAudience = "Urban professionals aged 25-45 seeking clean skincare.",
                tone = Tone.LUXURY,
                wordsToUse = "radiance, effortless, ritual, clinically proven",
                wordsToAvoid = "cheap, magical, overnight miracle",
                personality = "Sophisticated, authoritative yet warm",
                isActive = true
            )
        }
    }

    private val _userAccount = MutableStateFlow(
        UserAccount(
            name = "Alex Rivera",
            email = "alex@mybrand.com",
            planTier = PlanTier.FREE,
            generationsToday = 2,
            isLoggedIn = true
        )
    )
    val userAccount = _userAccount.asStateFlow()

    // Authentication & Session Architecture
    suspend fun login(email: String, pass: String): Result<UserAccount> {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) return Result.failure(IllegalArgumentException("Email cannot be empty"))
        if (pass.length < 4) return Result.failure(IllegalArgumentException("Password must be at least 4 characters"))

        val name = cleanEmail.substringBefore("@").replace(".", " ").capitalizeWords()
        val account = UserAccount(
            name = name,
            email = cleanEmail,
            planTier = _userAccount.value.planTier,
            generationsToday = _userAccount.value.generationsToday,
            isLoggedIn = true
        )
        _userAccount.value = account

        userDao.saveUser(
            UserEntity(
                id = cleanEmail,
                name = name,
                email = cleanEmail,
                planTier = account.planTier.name,
                isLoggedIn = true
            )
        )
        return Result.success(account)
    }

    suspend fun register(name: String, email: String, pass: String): Result<UserAccount> {
        val cleanName = name.trim().ifBlank { "User" }
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        if (pass.length < 6) return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))

        val account = UserAccount(
            name = cleanName,
            email = cleanEmail,
            planTier = PlanTier.FREE,
            generationsToday = 0,
            isLoggedIn = true
        )
        _userAccount.value = account

        userDao.saveUser(
            UserEntity(
                id = cleanEmail,
                name = cleanName,
                email = cleanEmail,
                planTier = "FREE",
                isLoggedIn = true
            )
        )
        return Result.success(account)
    }

    suspend fun recoverPassword(email: String): Result<String> {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) return Result.failure(IllegalArgumentException("Please enter a valid email"))
        return Result.success("Password recovery link sent to $cleanEmail. Please check your inbox.")
    }

    suspend fun logout() {
        _userAccount.value = _userAccount.value.copy(isLoggedIn = false)
        userDao.setLoggedIn("user_default", false)
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        _userAccount.value = _userAccount.value.copy(isLoggedIn = isLoggedIn)
    }

    suspend fun saveBrandVoice(voice: BrandVoice) {
        brandVoiceDao.saveBrandVoice(
            BrandVoiceEntity(
                id = 1,
                brandName = voice.brandName,
                description = voice.description,
                targetAudience = voice.targetAudience,
                tone = voice.tone.name,
                wordsToUse = voice.wordsToUse,
                wordsToAvoid = voice.wordsToAvoid,
                personality = voice.personality,
                isActive = voice.isActive
            )
        )
    }

    suspend fun saveContent(
        title: String,
        contentType: String,
        content: String,
        originalPrompt: String = "",
        platform: String = "",
        tags: String = "",
        projectId: Long = 0
    ): Long {
        return savedContentDao.insert(
            SavedContentEntity(
                title = title,
                contentType = contentType,
                content = content,
                originalPrompt = originalPrompt,
                platform = platform,
                tags = tags,
                projectId = projectId
            )
        )
    }

    suspend fun toggleFavorite(id: Long, currentVal: Boolean) {
        savedContentDao.updateFavorite(id, !currentVal)
    }

    suspend fun deleteContent(id: Long) {
        savedContentDao.deleteById(id)
    }

    suspend fun clearAllSaved() {
        savedContentDao.clearAll()
    }

    // Projects
    suspend fun createProject(name: String, description: String, category: String): Long {
        return projectDao.insert(
            ProjectEntity(
                name = name,
                description = description,
                category = category
            )
        )
    }

    suspend fun deleteProject(id: Long) {
        projectDao.deleteById(id)
    }

    fun incrementUsage() {
        _userAccount.value = _userAccount.value.copy(
            generationsToday = _userAccount.value.generationsToday + 1
        )
    }

    fun upgradePlan(tier: PlanTier) {
        _userAccount.value = _userAccount.value.copy(planTier = tier)
    }

    fun updateUser(name: String, email: String) {
        _userAccount.value = _userAccount.value.copy(name = name, email = email)
    }

    // AI Generation Methods
    suspend fun generateWriterCopy(
        productInfo: String,
        contentType: ContentType,
        targetAudience: String,
        tone: Tone,
        language: Language,
        length: ContentLength,
        cta: String,
        brandVoice: BrandVoice?
    ): String {
        incrementUsage()

        val promptBuilder = StringBuilder()
        promptBuilder.append("Create a professional marketing ${contentType.displayName} for:\n")
        promptBuilder.append("Product/Business: $productInfo\n")
        if (targetAudience.isNotBlank()) promptBuilder.append("Target Audience: $targetAudience\n")
        promptBuilder.append("Tone: ${tone.displayName}\n")
        promptBuilder.append("Language: ${language.displayName}\n")
        promptBuilder.append("Length: ${length.displayName}\n")
        if (cta.isNotBlank()) promptBuilder.append("Call to Action: $cta\n")

        if (brandVoice != null && brandVoice.isActive && brandVoice.brandName.isNotBlank()) {
            promptBuilder.append("\nBrand Guidelines to follow:\n")
            promptBuilder.append("- Brand Name: ${brandVoice.brandName}\n")
            promptBuilder.append("- Voice Personality: ${brandVoice.personality}\n")
            if (brandVoice.wordsToUse.isNotBlank()) promptBuilder.append("- Keywords to include: ${brandVoice.wordsToUse}\n")
            if (brandVoice.wordsToAvoid.isNotBlank()) promptBuilder.append("- Words to avoid: ${brandVoice.wordsToAvoid}\n")
        }

        promptBuilder.append("\nOutput ONLY the final, ready-to-publish copy without meta commentary.")

        val geminiResult = GeminiClient.generateContent(promptBuilder.toString())
        return if (geminiResult.isSuccess && geminiResult.getOrNull()?.isNotBlank() == true) {
            geminiResult.getOrNull()!!
        } else {
            MarketingEngine.generateWriterCopy(
                productInfo = productInfo,
                contentType = contentType,
                targetAudience = targetAudience,
                tone = tone,
                language = language,
                length = length,
                cta = cta,
                brandVoice = brandVoice
            )
        }
    }

    suspend fun generateProductDescription(
        name: String,
        category: String,
        features: String,
        benefits: String,
        price: String,
        audience: String,
        tone: Tone,
        language: Language
    ): ProductDescriptionResult {
        incrementUsage()
        return MarketingEngine.generateProductDescription(
            name = name,
            category = category,
            features = features,
            benefits = benefits,
            price = price,
            audience = audience,
            tone = tone,
            language = language
        )
    }

    suspend fun generateTikTok(
        topic: String,
        audience: String,
        goal: String,
        tone: Tone
    ): TikTokScriptResult {
        incrementUsage()
        return MarketingEngine.generateTikTok(topic, audience, goal, tone)
    }

    suspend fun generateInstagram(
        topic: String,
        audience: String,
        tone: Tone
    ): InstagramResult {
        incrementUsage()
        return MarketingEngine.generateInstagram(topic, audience, tone)
    }

    suspend fun generateAds(
        product: String,
        audience: String,
        goal: String,
        platform: AdPlatform,
        tone: Tone
    ): List<AdVariation> {
        incrementUsage()
        return MarketingEngine.generateAds(product, audience, goal, platform, tone)
    }

    suspend fun generateEmail(
        product: String,
        audience: String,
        goal: String,
        tone: Tone
    ): EmailResult {
        incrementUsage()
        return MarketingEngine.generateEmail(product, audience, goal, tone)
    }

    suspend fun generateHashtags(
        topic: String,
        niche: String
    ): HashtagsResult {
        incrementUsage()
        return MarketingEngine.generateHashtags(topic, niche)
    }

    suspend fun generateCampaign(
        product: String,
        description: String,
        audience: String,
        goal: String,
        offer: String,
        platforms: Set<String>,
        duration: String,
        tone: Tone
    ): FullCampaign {
        incrementUsage()

        val prompt = """
            Act as an elite Chief Marketing Officer. Build a high-converting marketing campaign for:
            - Product/Service: $product
            - Description: $description
            - Target Audience: $audience
            - Main Goal: $goal
            - Offer: $offer
            - Preferred Platforms: ${platforms.joinToString(", ")}
            - Duration: $duration
            - Brand Tone: ${tone.displayName}
            
            Deliver high-converting strategy, customer profile, angles, offer breakdown, USP, hooks, Instagram posts, TikTok scripts, Meta ads, Google search ads, email sequence, WhatsApp message, hashtags, and a 7-day content calendar.
        """.trimIndent()

        val geminiResult = GeminiClient.generateContent(prompt)
        val fullEngineCampaign = MarketingEngine.generateFullCampaign(
            product = product,
            description = description,
            audience = audience,
            goal = goal,
            offer = offer,
            platforms = platforms,
            duration = duration,
            tone = tone
        )

        return if (geminiResult.isSuccess && geminiResult.getOrNull()?.isNotBlank() == true) {
            val text = geminiResult.getOrNull()!!
            fullEngineCampaign.copy(
                strategy = if (text.length > 200) text.take(600).trim() else fullEngineCampaign.strategy
            )
        } else {
            fullEngineCampaign
        }
    }

    suspend fun regenerateSection(
        sectionType: CampaignSectionType,
        product: String,
        description: String,
        audience: String,
        tone: Tone
    ): String {
        incrementUsage()
        val prompt = "Generate a fresh, high-converting alternative for ${sectionType.title} for $product (Target Audience: $audience, Tone: ${tone.displayName}). Keep it punchy, polished and ready to publish."
        val geminiResult = GeminiClient.generateContent(prompt)
        if (geminiResult.isSuccess && geminiResult.getOrNull()?.isNotBlank() == true) {
            return geminiResult.getOrNull()!!
        }

        return when (sectionType) {
            CampaignSectionType.STRATEGY -> """
                • Fresh Omnichannel Strategy for $product:
                - Rapid validation testing on TikTok & Meta Ads with early test budgets.
                - Strategic influencer seeding with 15 micro-creators in the $audience space.
                - Automated high-retention email workflow triggering within 15 minutes of initial opt-in.
                - Primary metric: Target 3.5x Return on Ad Spend (ROAS).
            """.trimIndent()
            CampaignSectionType.CUSTOMER_PROFILE -> """
                • Primary Persona: High-agency $audience who value speed and reliability over complex tools.
                • Emotional Trigger: Overwhelmed by current disjointed workflows and looking for a cohesive, premium partner.
                • Buying Barrier: "Will this actually save me time or is it just more setup?"
                • Conversion Bridge: Instant time-to-value proof and ironclad money-back guarantee.
            """.trimIndent()
            CampaignSectionType.CORE_ANGLE -> "The 'Instant Competitive Edge' Angle: Framing $product not as an incremental tweak, but as the secret weapon that lets $audience achieve in 10 minutes what used to take all day."
            CampaignSectionType.MAIN_OFFER -> "Exclusive VIP Launch Bundle:\n• 25% Off Annual Pass\n• Free 1-on-1 Strategy Setup Call\n• Lifetime Access to Conversion Asset Library\n• 30-Day Zero-Risk Guarantee"
            CampaignSectionType.USP -> "The only solution built specifically for $audience that eliminates 80% of repetitive setup while increasing output quality."
            CampaignSectionType.HOOKS -> """
                1. 'If you're still doing $product manually, stop right now.'
                2. 'Here is what nobody tells you about getting results in $description.'
                3. 'POV: You found the tool that makes everyone ask how you scale so fast.'
            """.trimIndent()
            CampaignSectionType.INSTAGRAM_POSTS -> """
                [NEW CAROUSEL CONCEPT]
                Slide 1: The 3 biggest mistakes $audience make (and how $product fixes them)
                Slide 2: Mistake 1: Relying on generic templates
                Slide 3: Mistake 2: Ignoring platform-specific retention cues
                Slide 4: The Solution: Clean, conversion-focused execution with $product
                Slide 5: Link in bio for launch privileges!
            """.trimIndent()
            CampaignSectionType.TIKTOK_CONCEPTS -> """
                [NEW TIKTOK HOOK & SCRIPT]
                Hook (0-3s): 'Do not buy another tool until you see this test.'
                Visual: Side-by-side timer test comparing old method vs $product.
                Audio: Punchy trend track + confident voiceover.
                CTA: 'Check bio link before launch pricing closes!'
            """.trimIndent()
            CampaignSectionType.META_ADS -> """
                [HIGH CTR VARIATION]
                Headline: The Smarter Move for $audience
                Primary: Transform your output with zero learning curve. Join thousands who already upgraded to $product.
                CTA: Claim Special Offer
            """.trimIndent()
            CampaignSectionType.GOOGLE_ADS -> """
                [SEARCH AD - INTENT TARGETED]
                Headline 1: Switch to $product Today
                Headline 2: 30-Day Money-Back Guarantee
                Headline 3: Fast & Easy Setup
                Description: Engineered specifically for $audience. Rated 4.9/5 by industry leaders.
            """.trimIndent()
            CampaignSectionType.EMAILS -> """
                Subject: Quick question about your current setup...
                Preview: A better way to handle $description without the headache.
                
                Hey there,
                
                If you've been searching for a cleaner way to achieve top-tier results without sacrificing your weekend, this is for you.
                
                We just released our VIP launch package for $product.
                
                >> Check out the full breakdown and unlock early pricing <<
            """.trimIndent()
            CampaignSectionType.WHATSAPP -> "🔥 VIP Alert: Your early access code for $product is live! Get 20% off + free bonus resources for the next 24 hours only: https://mybrand.com/vip"
            CampaignSectionType.HASHTAGS -> "#$product #growthhacks #creatorlife #smallbusinesstips #launchstrategy #trending2026"
            CampaignSectionType.CONTENT_CALENDAR -> "📅 Day 1: Problem Teaser\n📅 Day 2: Official Launch\n📅 Day 3: Feature Deep-Dive\n📅 Day 4: Social Proof Carousel\n📅 Day 5: VIP WhatsApp Blast\n📅 Day 6: Comparison Video\n📅 Day 7: Scarcity Countdown"
        }
    }
}

private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
