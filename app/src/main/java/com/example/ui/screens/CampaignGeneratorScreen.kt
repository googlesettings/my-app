package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.CampaignSectionType
import com.example.model.FullCampaign
import com.example.model.Tone
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.SellAiGradients
import com.example.ui.viewmodel.SellAiViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CampaignGeneratorScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val product by viewModel.campaignProduct.collectAsStateWithLifecycle()
    val description by viewModel.campaignDescription.collectAsStateWithLifecycle()
    val audience by viewModel.campaignAudience.collectAsStateWithLifecycle()
    val goal by viewModel.campaignGoal.collectAsStateWithLifecycle()
    val offer by viewModel.campaignOffer.collectAsStateWithLifecycle()
    val platforms by viewModel.campaignPlatforms.collectAsStateWithLifecycle()
    val duration by viewModel.campaignDuration.collectAsStateWithLifecycle()
    val tone by viewModel.campaignTone.collectAsStateWithLifecycle()
    val isGenerating by viewModel.campaignIsGenerating.collectAsStateWithLifecycle()
    val campaignResult by viewModel.campaignResult.collectAsStateWithLifecycle()
    val editingSection by viewModel.editingSectionType.collectAsStateWithLifecycle()
    val editingText by viewModel.editingSectionContent.collectAsStateWithLifecycle()
    val isRegeneratingSection by viewModel.isRegeneratingSection.collectAsStateWithLifecycle()

    var showConfigPanel by remember { mutableStateOf(campaignResult == null) }
    var toneMenuExpanded by remember { mutableStateOf(false) }
    var goalMenuExpanded by remember { mutableStateOf(false) }
    var durationMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    val allPlatforms = listOf("Instagram", "TikTok", "Facebook", "Google", "Email", "WhatsApp", "LinkedIn", "X")
    val goalOptions = listOf("Product Launch & Direct Sales", "Lead Generation", "Brand Awareness & Reach", "Flash Sale & Conversions", "Waitlist & Crowdfunding")
    val durationOptions = listOf("3 Days", "7 Days", "14 Days", "30 Days")

    val categories = listOf("All (14)", "Strategy", "Creative", "Ads", "Outreach", "Calendar")

    // Filter sections based on selected category tab
    val displayedSections = remember(selectedCategoryIndex) {
        when (selectedCategoryIndex) {
            1 -> listOf(
                CampaignSectionType.STRATEGY,
                CampaignSectionType.CUSTOMER_PROFILE,
                CampaignSectionType.CORE_ANGLE,
                CampaignSectionType.MAIN_OFFER,
                CampaignSectionType.USP
            )
            2 -> listOf(
                CampaignSectionType.HOOKS,
                CampaignSectionType.INSTAGRAM_POSTS,
                CampaignSectionType.TIKTOK_CONCEPTS
            )
            3 -> listOf(
                CampaignSectionType.META_ADS,
                CampaignSectionType.GOOGLE_ADS
            )
            4 -> listOf(
                CampaignSectionType.EMAILS,
                CampaignSectionType.WHATSAPP,
                CampaignSectionType.HASHTAGS
            )
            5 -> listOf(
                CampaignSectionType.CONTENT_CALENDAR
            )
            else -> CampaignSectionType.values().toList()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("campaign_generator_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Banner
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SellAiGradients.brandGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Campaign Generator",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "360° End-to-End Multi-Channel Launch Engine",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyanAccent.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Gemini 3.5 Flash", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                            }
                        }
                    }

                    if (campaignResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Campaign: ",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = campaignResult?.productName ?: "",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { showConfigPanel = !showConfigPanel },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(34.dp)
                                        .testTag("toggle_config_btn")
                                ) {
                                    Icon(
                                        imageVector = if (showConfigPanel) Icons.Default.ExpandLess else Icons.Default.Tune,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (showConfigPanel) "Hide Inputs" else "Edit Inputs", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { viewModel.exportCampaign(context) },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(34.dp)
                                        .testTag("export_campaign_header_btn")
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Export Campaign", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Input Configuration Panel (Collapsible if result is generated)
        if (showConfigPanel || campaignResult == null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "CAMPAIGN PARAMETERS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary,
                            letterSpacing = 1.sp
                        )

                        // Preset Inspiration Chips
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fast Presets:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PresetChip("✨ LuxeGlow Skincare") {
                                viewModel.campaignProduct.value = "LuxeGlow Skincare"
                                viewModel.campaignDescription.value = "Organic peptide hydration cream with SPF 30 for effortless everyday glow."
                                viewModel.campaignAudience.value = "Women 25-45 looking for simple, natural anti-aging routine"
                                viewModel.campaignGoal.value = "Product Launch & Direct Sales"
                                viewModel.campaignOffer.value = "20% off launch week + free Jade Roller on orders over $50"
                                viewModel.campaignTone.value = Tone.LUXURY
                            }
                            PresetChip("⚡ FlowSync SaaS") {
                                viewModel.campaignProduct.value = "FlowSync B2B"
                                viewModel.campaignDescription.value = "AI-powered automated project tracking and client billing tool for creative agencies."
                                viewModel.campaignAudience.value = "Agency founders, freelance directors, and operations managers"
                                viewModel.campaignGoal.value = "Lead Generation"
                                viewModel.campaignOffer.value = "14-day unrestricted trial + 30% off annual plan"
                                viewModel.campaignTone.value = Tone.PROFESSIONAL
                            }
                            PresetChip("☕ Atlas Artisan Coffee") {
                                viewModel.campaignProduct.value = "Atlas Artisan Coffee"
                                viewModel.campaignDescription.value = "Direct-trade single-origin Ethiopian beans roasted weekly in micro-batches."
                                viewModel.campaignAudience.value = "Coffee enthusiasts & remote professionals working from home"
                                viewModel.campaignGoal.value = "Flash Sale & Conversions"
                                viewModel.campaignOffer.value = "Buy 2 bags get 1 free + free priority shipping"
                                viewModel.campaignTone.value = Tone.FRIENDLY
                            }
                        }

                        // Product or Service
                        Column {
                            Text("Product or Service *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = product,
                                onValueChange = { viewModel.campaignProduct.value = it },
                                placeholder = { Text("e.g. LuxeGlow Skincare, FlowSync App, Atlas Coffee...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("campaign_product_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        // Description
                        Column {
                            Text("Product / Service Description *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = description,
                                onValueChange = { viewModel.campaignDescription.value = it },
                                placeholder = { Text("Key features, how it works, what problems it solves...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(85.dp)
                                    .testTag("campaign_desc_input"),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        // Target Audience
                        Column {
                            Text("Target Audience *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = audience,
                                onValueChange = { viewModel.campaignAudience.value = it },
                                placeholder = { Text("e.g. Agency owners, busy moms, fitness beginners, creators...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("campaign_audience_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        // Main Goal & Offer Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Main Goal Dropdown
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Main Goal", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box {
                                    OutlinedButton(
                                        onClick = { goalMenuExpanded = true },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .testTag("campaign_goal_dropdown")
                                    ) {
                                        Text(goal, fontSize = 12.sp, maxLines = 1)
                                    }
                                    DropdownMenu(
                                        expanded = goalMenuExpanded,
                                        onDismissRequest = { goalMenuExpanded = false }
                                    ) {
                                        goalOptions.forEach { opt ->
                                            DropdownMenuItem(
                                                text = { Text(opt) },
                                                onClick = {
                                                    viewModel.campaignGoal.value = opt
                                                    goalMenuExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Campaign Duration
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Duration", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box {
                                    OutlinedButton(
                                        onClick = { durationMenuExpanded = true },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .testTag("campaign_duration_dropdown")
                                    ) {
                                        Text(duration, fontSize = 12.sp)
                                    }
                                    DropdownMenu(
                                        expanded = durationMenuExpanded,
                                        onDismissRequest = { durationMenuExpanded = false }
                                    ) {
                                        durationOptions.forEach { opt ->
                                            DropdownMenuItem(
                                                text = { Text(opt) },
                                                onClick = {
                                                    viewModel.campaignDuration.value = opt
                                                    durationMenuExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Offer
                        Column {
                            Text("Main Offer / Hook *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = offer,
                                onValueChange = { viewModel.campaignOffer.value = it },
                                placeholder = { Text("e.g. 20% off launch week, free gift on first order, 14-day free trial...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("campaign_offer_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        // Preferred Platforms Multi-Select
                        Column {
                            Text("Preferred Platforms", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                allPlatforms.forEach { platform ->
                                    val isSelected = platforms.contains(platform)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable { viewModel.toggleCampaignPlatform(platform) }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                            .testTag("platform_chip_$platform")
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                            }
                                            Text(
                                                text = platform,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Brand Tone
                        Column {
                            Text("Brand Tone", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                OutlinedButton(
                                    onClick = { toneMenuExpanded = true },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("campaign_tone_dropdown")
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(tone.displayName, fontSize = 13.sp)
                                        Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }
                                DropdownMenu(
                                    expanded = toneMenuExpanded,
                                    onDismissRequest = { toneMenuExpanded = false }
                                ) {
                                    Tone.values().forEach { t ->
                                        DropdownMenuItem(
                                            text = { Text(t.displayName) },
                                            onClick = {
                                                viewModel.campaignTone.value = t
                                                toneMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Generate Button
                        Button(
                            onClick = {
                                viewModel.generateCampaign()
                                showConfigPanel = false
                            },
                            enabled = !isGenerating && product.isNotBlank(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("generate_campaign_btn")
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Crafting 360° Campaign with Gemini...", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generate Full 360° Campaign", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Campaign Results Multi-Section Workspace
        if (campaignResult != null) {
            val campaign = campaignResult!!

            // Category Filter Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                ) {
                    categories.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedCategoryIndex == index,
                            onClick = { selectedCategoryIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.testTag("campaign_category_tab_$index")
                        )
                    }
                }
            }

            // Export Actions Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${displayedSections.size} Workspace Sections",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Every section has 1-tap Copy, Regenerate, Edit, and Save",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { viewModel.exportCampaign(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("export_campaign_main_btn")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export All (Markdown)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // The 14 Distinct Workspace Sections
            items(displayedSections) { sectionType ->
                val isRegeneratingThis = isRegeneratingSection == sectionType

                CampaignSectionCard(
                    sectionType = sectionType,
                    campaign = campaign,
                    isRegenerating = isRegeneratingThis,
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(sectionType.title, campaign.getSectionContent(sectionType))
                        clipboard.setPrimaryClip(clip)
                        viewModel.showToast("${sectionType.title} copied!")
                    },
                    onRegenerate = {
                        viewModel.regenerateCampaignSection(sectionType)
                    },
                    onEdit = {
                        viewModel.openEditSection(sectionType)
                    },
                    onSave = {
                        viewModel.saveCampaignSectionToLibrary(sectionType)
                    }
                )
            }

            // Bottom Export Banner
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Ready to launch this campaign?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Export the entire 14-section workspace to your clipboard, send via WhatsApp/Email, or save as a master plan in your library.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { viewModel.exportCampaign(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("export_campaign_footer_btn")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export Full Campaign", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Edit Section Dialog Modal
    if (editingSection != null) {
        var currentText by remember(editingSection) { mutableStateOf(editingText) }

        AlertDialog(
            onDismissRequest = { viewModel.closeEditSection() },
            title = {
                Text(
                    text = "Edit ${editingSection?.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Customize the copy for this section. Changes will update your active campaign.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = currentText,
                        onValueChange = { currentText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .testTag("edit_section_textfield"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.saveEditedSection(currentText) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("save_edit_section_btn")
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.closeEditSection() },
                    modifier = Modifier.testTag("cancel_edit_section_btn")
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PresetChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun CampaignSectionCard(
    sectionType: CampaignSectionType,
    campaign: FullCampaign,
    isRegenerating: Boolean,
    onCopy: () -> Unit,
    onRegenerate: () -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .testTag("section_card_${sectionType.name.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Title, Category Badge & Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = sectionType.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(IndigoPrimary.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = sectionType.platformTag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IndigoPrimary
                        )
                    }
                }

                // 4 Required Action Buttons: Copy, Regenerate, Edit, Save
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Copy Button
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("copy_section_${sectionType.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy section",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 2. Regenerate Button
                    IconButton(
                        onClick = onRegenerate,
                        enabled = !isRegenerating,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("regenerate_section_${sectionType.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Regenerate section",
                            modifier = Modifier
                                .size(16.dp)
                                .then(if (isRegenerating) Modifier.rotate(angle) else Modifier),
                            tint = if (isRegenerating) CyanAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 3. Edit Button
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("edit_section_${sectionType.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit section",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 4. Save Button
                    IconButton(
                        onClick = onSave,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("save_section_${sectionType.name.lowercase()}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Save section",
                            modifier = Modifier.size(16.dp),
                            tint = EmeraldSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Body Display
            if (isRegenerating) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = IndigoPrimary
                    )
                }
            } else {
                when (sectionType) {
                    CampaignSectionType.HOOKS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.hooks.forEachIndexed { idx, hook ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = hook,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.INSTAGRAM_POSTS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.instagramPosts.forEachIndexed { idx, post ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = post,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.TIKTOK_CONCEPTS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.tiktokConcepts.forEachIndexed { idx, concept ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = concept,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.META_ADS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.metaAds.forEachIndexed { idx, ad ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = ad,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.GOOGLE_ADS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.googleAds.forEachIndexed { idx, ad ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = ad,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.EMAILS -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.emails.forEachIndexed { idx, email ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = email,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    CampaignSectionType.CONTENT_CALENDAR -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            campaign.contentCalendar.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${day.dayName} • ${day.platform}",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = IndigoPrimary
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(CyanAccent.copy(alpha = 0.15f))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = day.bestTimeToPost,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = CyanAccent
                                                )
                                            }
                                        }
                                        Text(
                                            text = day.postTheme,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = day.contentSummary,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        // Standard formatted text view
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = campaign.getSectionContent(sectionType),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
