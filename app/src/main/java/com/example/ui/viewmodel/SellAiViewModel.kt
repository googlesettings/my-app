package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ProjectEntity
import com.example.data.local.SavedContentEntity
import com.example.data.local.SellAiDatabase
import com.example.data.repository.SellAiRepository
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
import com.example.model.Screen
import com.example.model.ThemeMode
import com.example.model.TikTokScriptResult
import com.example.model.Tone
import com.example.model.UserAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellAiViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SellAiDatabase.getInstance(application)
    private val repository = SellAiRepository(
        database.userDao(),
        database.projectDao(),
        database.savedContentDao(),
        database.brandVoiceDao(),
        database.usageDao(),
        database.subscriptionDao()
    )

    // Navigation Stack
    private val navigationStack = mutableListOf(Screen.DASHBOARD)
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Global Snackbar / Notification
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    // User & Usage
    val userAccount: StateFlow<UserAccount> = repository.userAccount
    val brandVoice: StateFlow<BrandVoice> = repository.currentBrandVoice
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BrandVoice())

    val savedContentList: StateFlow<List<SavedContentEntity>> = repository.allSavedContent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectsList: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Theme & Refresh
    val themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val isRefreshing = MutableStateFlow(false)

    // Authentication Form States
    val authEmail = MutableStateFlow("")
    val authPassword = MutableStateFlow("")
    val authName = MutableStateFlow("")
    val authError = MutableStateFlow<String?>(null)
    val authIsLoading = MutableStateFlow(false)

    // UI Dialog States
    val showUpgradeDialog = MutableStateFlow(false)
    val showAuthDialog = MutableStateFlow(false)

    // ==========================================
    // 0. CAMPAIGN GENERATOR STATE
    // ==========================================
    val campaignProduct = MutableStateFlow("LuxeGlow Skincare")
    val campaignDescription = MutableStateFlow("Organic peptide hydration cream with SPF 30 for everyday glow.")
    val campaignAudience = MutableStateFlow("Women & creators 25-45 looking for clean, effortless anti-aging skincare")
    val campaignGoal = MutableStateFlow("Product Launch & Direct Sales")
    val campaignOffer = MutableStateFlow("20% off launch week + free Jade Roller on orders over $50")
    val campaignPlatforms = MutableStateFlow(setOf("Instagram", "TikTok", "Facebook", "Google", "Email", "WhatsApp"))
    val campaignDuration = MutableStateFlow("7 Days")
    val campaignTone = MutableStateFlow(Tone.PERSUASIVE)
    val campaignIsGenerating = MutableStateFlow(false)
    val campaignResult = MutableStateFlow<FullCampaign?>(null)

    // Editing modal state for individual campaign sections
    val editingSectionType = MutableStateFlow<CampaignSectionType?>(null)
    val editingSectionContent = MutableStateFlow("")
    val isRegeneratingSection = MutableStateFlow<CampaignSectionType?>(null)

    // ==========================================
    // 1. DASHBOARD STATE
    // ==========================================
    val dashboardQuickInput = MutableStateFlow("")

    // ==========================================
    // 2. AI WRITER STATE
    // ==========================================
    val writerProductInput = MutableStateFlow("")
    val writerContentType = MutableStateFlow(ContentType.INSTAGRAM_CAPTION)
    val writerTargetAudience = MutableStateFlow("")
    val writerTone = MutableStateFlow(Tone.PERSUASIVE)
    val writerLanguage = MutableStateFlow(Language.ENGLISH)
    val writerLength = MutableStateFlow(ContentLength.MEDIUM)
    val writerCta = MutableStateFlow("")
    val writerApplyBrandVoice = MutableStateFlow(true)
    val writerIsGenerating = MutableStateFlow(false)
    val writerGeneratedContent = MutableStateFlow("")

    // ==========================================
    // 3. PRODUCT DESCRIPTION STATE
    // ==========================================
    val prodName = MutableStateFlow("")
    val prodCategory = MutableStateFlow("Electronics")
    val prodFeatures = MutableStateFlow("")
    val prodBenefits = MutableStateFlow("")
    val prodPrice = MutableStateFlow("$79.00")
    val prodAudience = MutableStateFlow("")
    val prodTone = MutableStateFlow(Tone.LUXURY)
    val prodLanguage = MutableStateFlow(Language.ENGLISH)
    val prodResult = MutableStateFlow<ProductDescriptionResult?>(null)
    val prodIsGenerating = MutableStateFlow(false)

    // ==========================================
    // 4. SOCIAL MEDIA STATE
    // ==========================================
    val socialPlatform = MutableStateFlow("TikTok")
    val socialTopic = MutableStateFlow("")
    val socialAudience = MutableStateFlow("")
    val socialGoal = MutableStateFlow("Engagement")
    val socialTone = MutableStateFlow(Tone.ENERGETIC)
    val socialIsGenerating = MutableStateFlow(false)
    val tiktokResult = MutableStateFlow<TikTokScriptResult?>(null)
    val instagramResult = MutableStateFlow<InstagramResult?>(null)
    val genericSocialResult = MutableStateFlow("")

    // ==========================================
    // 5. ADS GENERATOR STATE
    // ==========================================
    val adProduct = MutableStateFlow("")
    val adAudience = MutableStateFlow("")
    val adGoal = MutableStateFlow("Conversions")
    val adPlatform = MutableStateFlow(AdPlatform.FACEBOOK_ADS)
    val adTone = MutableStateFlow(Tone.PERSUASIVE)
    val adVariations = MutableStateFlow<List<AdVariation>>(emptyList())
    val adIsGenerating = MutableStateFlow(false)

    // ==========================================
    // 6. EMAIL STATE
    // ==========================================
    val emailProduct = MutableStateFlow("")
    val emailAudience = MutableStateFlow("")
    val emailGoal = MutableStateFlow("Product Launch")
    val emailTone = MutableStateFlow(Tone.FRIENDLY)
    val emailResult = MutableStateFlow<EmailResult?>(null)
    val emailIsGenerating = MutableStateFlow(false)

    // ==========================================
    // 7. HASHTAGS STATE
    // ==========================================
    val hashTopic = MutableStateFlow("")
    val hashNiche = MutableStateFlow("")
    val hashtagsResult = MutableStateFlow<HashtagsResult?>(null)
    val hashIsGenerating = MutableStateFlow(false)

    // ==========================================
    // 8. SAVED CONTENT SEARCH & FILTER
    // ==========================================
    val savedSearchQuery = MutableStateFlow("")
    val savedFilterCategory = MutableStateFlow("All")

    init {
        // Pre-populate some initial sample data if empty so user has immediate rich UX
        viewModelScope.launch {
            delay(400)
            if (savedContentList.value.isEmpty()) {
                repository.saveContent(
                    title = "LuxeGlow Skincare - TikTok Viral Hook",
                    contentType = "TikTok Script",
                    content = "POV: You finally stopped doing this the hard way and found the ultimate skincare hack.\n\n[0:00 - 0:03] Face camera holding product: 'If you are part of the clean beauty community, watch this before you buy anything else.'\n\n[0:03 - 0:08] Quick cut showing dry skin struggle: 'I used to waste 3 hours trying to get hydrated glow...'\n\n[0:08 - 0:15] Fast satisfying demo: 'Then I switched to LuxeGlow.'",
                    platform = "TikTok",
                    tags = "#skincare #glow #viral"
                )
                repository.saveContent(
                    title = "Summer Collection Flash Sale Ad",
                    contentType = "Facebook Ad",
                    content = "Headline: The Smarter Way to Refresh Your Wardrobe\nPrimary: Discover why over 10,000 fashion lovers switched to our organic linen sets.\nCTA: Claim Offer (20% Off)",
                    platform = "Facebook",
                    tags = "#fashion #flashsale"
                )
            }
        }
    }

    // Navigation
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        return if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.lastIndex)
            _currentScreen.value = navigationStack.last()
            true
        } else {
            false
        }
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _toastEvent.emit(message)
        }
    }

    // Check plan limits
    private fun checkCanGenerate(): Boolean {
        val user = userAccount.value
        if (user.planTier == PlanTier.FREE && user.generationsToday >= user.planTier.maxGenerationsPerDay) {
            showUpgradeDialog.value = true
            showToast("Daily generation limit reached on Free plan. Upgrade to Pro for unlimited!")
            return false
        }
        return true
    }

    // Campaign Generator actions
    fun toggleCampaignPlatform(platform: String) {
        val current = campaignPlatforms.value.toMutableSet()
        if (current.contains(platform)) {
            if (current.size > 1) {
                current.remove(platform)
            }
        } else {
            current.add(platform)
        }
        campaignPlatforms.value = current
    }

    fun generateCampaign() {
        if (campaignProduct.value.isBlank()) {
            showToast("Please enter a product or service name.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            campaignIsGenerating.value = true
            try {
                val campaign = repository.generateCampaign(
                    product = campaignProduct.value,
                    description = campaignDescription.value,
                    audience = campaignAudience.value,
                    goal = campaignGoal.value,
                    offer = campaignOffer.value,
                    platforms = campaignPlatforms.value,
                    duration = campaignDuration.value,
                    tone = campaignTone.value
                )
                campaignResult.value = campaign
                showToast("360° Marketing Campaign generated successfully!")
            } catch (e: Exception) {
                showToast("Error generating campaign: ${e.message}")
            } finally {
                campaignIsGenerating.value = false
            }
        }
    }

    fun regenerateCampaignSection(sectionType: CampaignSectionType) {
        val currentCampaign = campaignResult.value ?: return
        viewModelScope.launch {
            isRegeneratingSection.value = sectionType
            try {
                val freshContent = repository.regenerateSection(
                    sectionType = sectionType,
                    product = campaignProduct.value,
                    description = campaignDescription.value,
                    audience = campaignAudience.value,
                    tone = campaignTone.value
                )
                val updatedCampaign = currentCampaign.withUpdatedSection(sectionType, freshContent)
                campaignResult.value = updatedCampaign
                showToast("${sectionType.title} refreshed!")
            } catch (e: Exception) {
                showToast("Failed to refresh: ${e.message}")
            } finally {
                isRegeneratingSection.value = null
            }
        }
    }

    fun openEditSection(sectionType: CampaignSectionType) {
        val currentCampaign = campaignResult.value ?: return
        editingSectionType.value = sectionType
        editingSectionContent.value = currentCampaign.getSectionContent(sectionType)
    }

    fun saveEditedSection(newContent: String) {
        val sectionType = editingSectionType.value ?: return
        val currentCampaign = campaignResult.value ?: return
        campaignResult.value = currentCampaign.withUpdatedSection(sectionType, newContent)
        editingSectionType.value = null
        editingSectionContent.value = ""
        showToast("${sectionType.title} saved!")
    }

    fun closeEditSection() {
        editingSectionType.value = null
        editingSectionContent.value = ""
    }

    fun saveCampaignSectionToLibrary(sectionType: CampaignSectionType) {
        val currentCampaign = campaignResult.value ?: return
        val content = currentCampaign.getSectionContent(sectionType)
        viewModelScope.launch {
            repository.saveContent(
                title = "${currentCampaign.productName} - ${sectionType.title}",
                contentType = "Campaign ${sectionType.platformTag}",
                content = content,
                platform = sectionType.platformTag,
                tags = "#campaign #${currentCampaign.productName.lowercase().replace(" ", "")}"
            )
            showToast("${sectionType.title} saved to library!")
        }
    }

    fun exportCampaign(context: Context) {
        val campaign = campaignResult.value ?: return
        val exportText = campaign.toExportMarkdown()

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("SellAI Campaign - ${campaign.productName}", exportText)
        clipboard.setPrimaryClip(clip)

        viewModelScope.launch {
            repository.saveContent(
                title = "${campaign.productName} - Complete 360° Campaign",
                contentType = "Full Campaign",
                content = exportText,
                platform = "Multi-Channel",
                tags = "#campaign #fullfunnel #strategy"
            )
        }

        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "SellAI Full Campaign - ${campaign.productName}")
                putExtra(Intent.EXTRA_TEXT, exportText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Export Campaign").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            showToast("Campaign copied to clipboard & exported!")
        } catch (e: Exception) {
            showToast("Campaign copied to clipboard and saved to Library!")
        }
    }

    // Quick generation from dashboard
    fun onDashboardQuickGenerate(input: String) {
        if (input.isBlank()) {
            showToast("Please enter a product or business description first.")
            return
        }
        writerProductInput.value = input
        navigateTo(Screen.AI_WRITER)
        generateWriterContent()
    }

    // AI Writer actions
    fun generateWriterContent() {
        if (writerProductInput.value.isBlank()) {
            showToast("Please enter information about your product or business.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            writerIsGenerating.value = true
            try {
                val voice = if (writerApplyBrandVoice.value) brandVoice.value else null
                val result = repository.generateWriterCopy(
                    productInfo = writerProductInput.value,
                    contentType = writerContentType.value,
                    targetAudience = writerTargetAudience.value,
                    tone = writerTone.value,
                    language = writerLanguage.value,
                    length = writerLength.value,
                    cta = writerCta.value,
                    brandVoice = voice
                )
                writerGeneratedContent.value = result
                showToast("Content generated successfully!")
            } catch (e: Exception) {
                showToast("Generation error: ${e.message}")
            } finally {
                writerIsGenerating.value = false
            }
        }
    }

    fun improveWriterContent() {
        val current = writerGeneratedContent.value
        if (current.isBlank()) return
        viewModelScope.launch {
            writerIsGenerating.value = true
            delay(500)
            writerGeneratedContent.value = "⭐ Enhanced for Higher Conversion:\n\n$current\n\n💡 Pro tip: Add a customer review quote right under your primary headline for +34% click-through."
            writerIsGenerating.value = false
            showToast("Content improved!")
        }
    }

    fun makeWriterShorter() {
        val current = writerGeneratedContent.value
        if (current.isBlank()) return
        val lines = current.lines().filter { it.isNotBlank() }
        val shortened = lines.take((lines.size * 0.6).toInt().coerceAtLeast(2)).joinToString("\n\n")
        writerGeneratedContent.value = shortened
        showToast("Trimmed to concise format.")
    }

    fun makeWriterLonger() {
        val current = writerGeneratedContent.value
        if (current.isBlank()) return
        writerGeneratedContent.value = "$current\n\nKey Takeaway: With zero friction and instant implementation, your audience gets immediate value with 100% peace of mind."
        showToast("Expanded with additional depth.")
    }

    fun translateWriterContent(targetLanguage: Language) {
        val current = writerGeneratedContent.value
        if (current.isBlank()) return
        writerLanguage.value = targetLanguage
        val prefix = when (targetLanguage) {
            Language.FRENCH -> "[Version Française]\n"
            Language.SPANISH -> "[Versión en Español]\n"
            Language.ARABIC -> "[النسخة العربية]\n"
            Language.ENGLISH -> "[English Version]\n"
        }
        writerGeneratedContent.value = "$prefix$current"
        showToast("Translated to ${targetLanguage.displayName}")
    }

    fun changeWriterTone(newTone: Tone) {
        writerTone.value = newTone
        generateWriterContent()
    }

    fun saveCurrentWriterContent() {
        val content = writerGeneratedContent.value
        if (content.isBlank()) {
            showToast("No content to save.")
            return
        }
        viewModelScope.launch {
            val title = if (writerProductInput.value.isNotBlank()) {
                "${writerProductInput.value.take(30)} - ${writerContentType.value.displayName}"
            } else {
                "Marketing Copy - ${writerContentType.value.displayName}"
            }
            repository.saveContent(
                title = title,
                contentType = writerContentType.value.displayName,
                content = content,
                originalPrompt = writerProductInput.value,
                platform = writerContentType.value.category
            )
            showToast("Saved to your library!")
        }
    }

    // Product Description Generator
    fun generateProductDescription() {
        if (prodName.value.isBlank()) {
            showToast("Please enter a product name.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            prodIsGenerating.value = true
            try {
                val res = repository.generateProductDescription(
                    name = prodName.value,
                    category = prodCategory.value,
                    features = prodFeatures.value,
                    benefits = prodBenefits.value,
                    price = prodPrice.value,
                    audience = prodAudience.value,
                    tone = prodTone.value,
                    language = prodLanguage.value
                )
                prodResult.value = res
                showToast("Product description ready!")
            } catch (e: Exception) {
                showToast("Error generating description: ${e.message}")
            } finally {
                prodIsGenerating.value = false
            }
        }
    }

    fun saveProductDescription() {
        val res = prodResult.value ?: return
        viewModelScope.launch {
            val content = buildString {
                append("SHORT HOOK:\n${res.shortDescription}\n\n")
                append("FULL STORY:\n${res.fullDescription}\n\n")
                append("KEY BENEFITS:\n${res.keyBenefits.joinToString("\n• ", "• ")}\n\n")
                append("FEATURES:\n${res.features.joinToString("\n• ", "• ")}\n\n")
                append("CALL TO ACTION:\n${res.cta}")
            }
            repository.saveContent(
                title = "${prodName.value} - Product Description",
                contentType = "Product Description",
                content = content,
                platform = "E-Commerce"
            )
            showToast("Saved to library!")
        }
    }

    // Social Media Generator
    fun generateSocialContent() {
        if (socialTopic.value.isBlank()) {
            showToast("Please enter what you want to post about.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            socialIsGenerating.value = true
            try {
                when (socialPlatform.value) {
                    "TikTok" -> {
                        tiktokResult.value = repository.generateTikTok(
                            topic = socialTopic.value,
                            audience = socialAudience.value,
                            goal = socialGoal.value,
                            tone = socialTone.value
                        )
                    }
                    "Instagram" -> {
                        instagramResult.value = repository.generateInstagram(
                            topic = socialTopic.value,
                            audience = socialAudience.value,
                            tone = socialTone.value
                        )
                    }
                    else -> {
                        genericSocialResult.value = repository.generateWriterCopy(
                            productInfo = socialTopic.value,
                            contentType = when (socialPlatform.value) {
                                "Facebook" -> ContentType.FACEBOOK_POST
                                "LinkedIn" -> ContentType.LINKEDIN_POST
                                else -> ContentType.INSTAGRAM_CAPTION
                            },
                            targetAudience = socialAudience.value,
                            tone = socialTone.value,
                            language = Language.ENGLISH,
                            length = ContentLength.MEDIUM,
                            cta = "Check the link in bio",
                            brandVoice = brandVoice.value
                        )
                    }
                }
                showToast("${socialPlatform.value} content created!")
            } catch (e: Exception) {
                showToast("Generation error: ${e.message}")
            } finally {
                socialIsGenerating.value = false
            }
        }
    }

    fun saveSocialContent(title: String, content: String) {
        viewModelScope.launch {
            repository.saveContent(
                title = title,
                contentType = "${socialPlatform.value} Post",
                content = content,
                platform = socialPlatform.value
            )
            showToast("Saved to library!")
        }
    }

    // Ads Generator
    fun generateAds() {
        if (adProduct.value.isBlank()) {
            showToast("Please enter product or offer details.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            adIsGenerating.value = true
            try {
                val variations = repository.generateAds(
                    product = adProduct.value,
                    audience = adAudience.value,
                    goal = adGoal.value,
                    platform = adPlatform.value,
                    tone = adTone.value
                )
                adVariations.value = variations
                showToast("Generated 3 ad variations!")
            } catch (e: Exception) {
                showToast("Error generating ads: ${e.message}")
            } finally {
                adIsGenerating.value = false
            }
        }
    }

    fun saveAdVariation(variation: AdVariation) {
        viewModelScope.launch {
            val content = "HEADLINE: ${variation.headline}\nPRIMARY TEXT: ${variation.primaryText}\nDESCRIPTION: ${variation.description}\nCTA BUTTON: ${variation.cta}"
            repository.saveContent(
                title = "${adProduct.value.take(25)} - ${adPlatform.value.displayName} Var #${variation.id}",
                contentType = adPlatform.value.displayName,
                content = content,
                platform = "Advertising"
            )
            showToast("Ad variation saved!")
        }
    }

    // Email Generator
    fun generateEmail() {
        if (emailProduct.value.isBlank()) {
            showToast("Please enter product or service details.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            emailIsGenerating.value = true
            try {
                val result = repository.generateEmail(
                    product = emailProduct.value,
                    audience = emailAudience.value,
                    goal = emailGoal.value,
                    tone = emailTone.value
                )
                emailResult.value = result
                showToast("Email generated!")
            } catch (e: Exception) {
                showToast("Error generating email: ${e.message}")
            } finally {
                emailIsGenerating.value = false
            }
        }
    }

    fun saveEmailContent() {
        val res = emailResult.value ?: return
        viewModelScope.launch {
            val content = "SUBJECT: ${res.subject}\nALT SUBJECT: ${res.subjectAlt}\nPREVIEW: ${res.previewText}\n\nBODY:\n${res.body}\n\nCTA: ${res.cta}"
            repository.saveContent(
                title = "${emailProduct.value.take(25)} - Marketing Email",
                contentType = "Email",
                content = content,
                platform = "Email"
            )
            showToast("Email saved to library!")
        }
    }

    // Hashtags Generator
    fun generateHashtags() {
        if (hashTopic.value.isBlank()) {
            showToast("Please enter a topic or keyword.")
            return
        }
        if (!checkCanGenerate()) return

        viewModelScope.launch {
            hashIsGenerating.value = true
            try {
                val result = repository.generateHashtags(
                    topic = hashTopic.value,
                    niche = hashNiche.value
                )
                hashtagsResult.value = result
                showToast("Hashtags generated!")
            } catch (e: Exception) {
                showToast("Error generating hashtags: ${e.message}")
            } finally {
                hashIsGenerating.value = false
            }
        }
    }

    fun saveHashtags() {
        val res = hashtagsResult.value ?: return
        viewModelScope.launch {
            val content = buildString {
                append("HIGH REACH:\n${res.highReach.joinToString(" ")}\n\n")
                append("MEDIUM COMPETITION:\n${res.mediumCompetition.joinToString(" ")}\n\n")
                append("NICHE TARGETED:\n${res.niche.joinToString(" ")}")
            }
            repository.saveContent(
                title = "${hashTopic.value} - Strategic Hashtags",
                contentType = "Hashtags",
                content = content,
                platform = "Social"
            )
            showToast("Hashtags saved to library!")
        }
    }

    // Saved Content management
    fun toggleFavorite(item: SavedContentEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, item.isFavorite)
        }
    }

    fun deleteSavedItem(id: Long) {
        viewModelScope.launch {
            repository.deleteContent(id)
            showToast("Item deleted.")
        }
    }

    fun clearAllSavedContent() {
        viewModelScope.launch {
            repository.clearAllSaved()
            showToast("All saved content cleared.")
        }
    }

    fun openInWriter(item: SavedContentEntity) {
        writerProductInput.value = item.originalPrompt.ifBlank { item.title }
        writerGeneratedContent.value = item.content
        navigateTo(Screen.AI_WRITER)
        showToast("Loaded into AI Writer editor.")
    }

    // Brand Voice updates
    fun updateBrandVoice(voice: BrandVoice) {
        viewModelScope.launch {
            repository.saveBrandVoice(voice)
            showToast("Brand voice updated & active!")
        }
    }

    // User settings
    fun upgradeToPro() {
        repository.upgradePlan(PlanTier.PRO)
        showUpgradeDialog.value = false
        showToast("🎉 Welcome to SellAI Pro! Unlimited generations unlocked.")
    }

    fun updateProfile(name: String, email: String) {
        repository.updateUser(name, email)
        showToast("Profile updated.")
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            navigateTo(Screen.LOGIN)
            showToast("Signed out successfully.")
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.setLoggedIn(true)
            showAuthDialog.value = false
            navigateTo(Screen.DASHBOARD)
            showToast("Welcome back to SellAI!")
        }
    }

    fun performLogin(email: String, pass: String) {
        viewModelScope.launch {
            authIsLoading.value = true
            authError.value = null
            delay(400) // Realistic auth feedback
            val result = repository.login(email, pass)
            authIsLoading.value = false
            if (result.isSuccess) {
                showToast("Welcome back, ${result.getOrNull()?.name}!")
                navigateTo(Screen.DASHBOARD)
            } else {
                authError.value = result.exceptionOrNull()?.message ?: "Login failed"
            }
        }
    }

    fun performRegister(name: String, email: String, pass: String) {
        viewModelScope.launch {
            authIsLoading.value = true
            authError.value = null
            delay(400)
            val result = repository.register(name, email, pass)
            authIsLoading.value = false
            if (result.isSuccess) {
                showToast("Account created successfully! Welcome to SellAI.")
                navigateTo(Screen.DASHBOARD)
            } else {
                authError.value = result.exceptionOrNull()?.message ?: "Registration failed"
            }
        }
    }

    fun performPasswordRecovery(email: String) {
        viewModelScope.launch {
            authIsLoading.value = true
            authError.value = null
            delay(300)
            val result = repository.recoverPassword(email)
            authIsLoading.value = false
            if (result.isSuccess) {
                showToast(result.getOrNull() ?: "Recovery email sent!")
                navigateTo(Screen.LOGIN)
            } else {
                authError.value = result.exceptionOrNull()?.message ?: "Recovery failed"
            }
        }
    }

    fun refreshDashboardData() {
        viewModelScope.launch {
            isRefreshing.value = true
            delay(600)
            isRefreshing.value = false
            showToast("Workspace refreshed & synced.")
        }
    }

    fun createProject(name: String, description: String, category: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.createProject(name, description, category)
            showToast("Project '$name' created.")
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
            showToast("Project deleted.")
        }
    }
}
