package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AdPlatform
import com.example.model.AdVariation
import com.example.ui.components.AiGeneratingCard
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.SellAiGradients
import com.example.ui.viewmodel.SellAiViewModel

@Composable
fun AdsScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val product by viewModel.adProduct.collectAsStateWithLifecycle()
    val audience by viewModel.adAudience.collectAsStateWithLifecycle()
    val goal by viewModel.adGoal.collectAsStateWithLifecycle()
    val platform by viewModel.adPlatform.collectAsStateWithLifecycle()
    val variations by viewModel.adVariations.collectAsStateWithLifecycle()
    val isGenerating by viewModel.adIsGenerating.collectAsStateWithLifecycle()
    val brandVoice by viewModel.brandVoice.collectAsStateWithLifecycle()

    val platformList = AdPlatform.values().toList()
    val selectedPlatformIndex = platformList.indexOf(platform).coerceAtLeast(0)

    val goals = listOf("Conversions / Sales", "Traffic & Clicks", "Lead Generation", "Brand Awareness")
    var showGoalMenu by remember { mutableStateOf(false) }
    var viewModeTab by remember { mutableIntStateOf(0) } // 0 = Copy View, 1 = Mockup Preview

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("ads_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "AI Advertising Generator",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Synthesizes 3 distinct marketing angles with headlines, primary text, and recommended CTAs.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Platform Tab Row
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedPlatformIndex,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            ) {
                platformList.forEachIndexed { index, p ->
                    Tab(
                        selected = selectedPlatformIndex == index,
                        onClick = { viewModel.adPlatform.value = p },
                        text = {
                            Text(
                                text = p.displayName,
                                fontWeight = if (selectedPlatformIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Form Inputs
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Product / Offer / Angle *", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = {
                                viewModel.adProduct.value = "Smart ergonomic standing desk with dual electric motor and memory presets"
                                viewModel.adAudience.value = "Remote software engineers and designers"
                            }
                        ) {
                            Text("Fill Example", fontSize = 11.sp, color = IndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = product,
                        onValueChange = { viewModel.adProduct.value = it },
                        placeholder = { Text("e.g. Ergonomic standing desk with memory presets, 30-day risk-free trial...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .testTag("ad_product_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Target Audience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = audience,
                                onValueChange = { viewModel.adAudience.value = it },
                                placeholder = { Text("Remote tech workers") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("ad_audience_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Campaign Goal", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                OutlinedTextField(
                                    value = goal.take(15),
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { showGoalMenu = true }) {
                                            Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showGoalMenu = true },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                DropdownMenu(
                                    expanded = showGoalMenu,
                                    onDismissRequest = { showGoalMenu = false }
                                ) {
                                    goals.forEach { g ->
                                        DropdownMenuItem(
                                            text = { Text(g) },
                                            onClick = {
                                                viewModel.adGoal.value = g
                                                showGoalMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateAds() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_ads_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate 3 High-ROI Ad Angles", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is synthesizing 3 distinct ad angles for ${platform.displayName}...")
            }
        }

        // Output Section
        if (variations.isNotEmpty() && !isGenerating) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "3 Tested Variations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // View Mode Switcher: Copy vs Live Feed Mockup
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (viewModeTab == 0) MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { viewModeTab = 0 }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Copy View", fontSize = 11.sp, fontWeight = if (viewModeTab == 0) FontWeight.Bold else FontWeight.Normal)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (viewModeTab == 1) MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { viewModeTab = 1 }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Feed Mockup", fontSize = 11.sp, fontWeight = if (viewModeTab == 1) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            items(variations) { variation ->
                if (viewModeTab == 0) {
                    AdVariationCard(
                        variation = variation,
                        onSave = { viewModel.saveAdVariation(variation) },
                        onCopy = { viewModel.showToast(it) }
                    )
                } else {
                    LiveFeedAdMockup(
                        variation = variation,
                        brandName = if (brandVoice.isActive && brandVoice.brandName.isNotBlank()) brandVoice.brandName else "SellAI Brand",
                        onCopy = { viewModel.showToast(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdVariationCard(
    variation: AdVariation,
    onSave: () -> Unit,
    onCopy: (String) -> Unit
) {
    val angleTitle = when (variation.id) {
        1 -> "Angle 1: Direct Value & ROI"
        2 -> "Angle 2: Pain Point / Problem-Solution"
        else -> "Angle 3: Social Proof & Urgency"
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(IndigoPrimary.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = angleTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )
                }

                Row {
                    CopyButton(
                        textToCopy = "${variation.headline}\n\n${variation.primaryText}\n\n${variation.description}\n\nCTA: ${variation.cta}",
                        onCopied = onCopy
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(onClick = onSave, shape = RoundedCornerShape(8.dp), modifier = Modifier.height(34.dp)) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Headline
            Text("Headline:", style = MaterialTheme.typography.labelSmall, color = CyanAccent, fontWeight = FontWeight.Bold)
            Text(variation.headline, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Text
            Text("Primary Copy:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(variation.primaryText, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text("Link Description:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(variation.description, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Recommended CTA Button: ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(variation.cta, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun LiveFeedAdMockup(
    variation: AdVariation,
    brandName: String,
    onCopy: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Avatar, Name, Sponsored badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SellAiGradients.brandGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(brandName.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(brandName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sponsored • ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Icon(imageVector = Icons.Default.Public, contentDescription = null, modifier = Modifier.size(10.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary text
            Text(variation.primaryText, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(10.dp))

            // Simulated Creative Image Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SellAiGradients.brandGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = variation.headline,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Feed footer: URL, Headline, and Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("mybrand.com/offer", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(variation.headline, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onCopy("${variation.headline}\n\n${variation.primaryText}\n\nCTA: ${variation.cta}") },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(variation.cta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
