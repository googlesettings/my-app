package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BrandVoice
import com.example.model.Tone
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.SellAiViewModel

data class VoicePreset(
    val title: String,
    val brandName: String,
    val description: String,
    val audience: String,
    val tone: Tone,
    val wordsToUse: String,
    val wordsToAvoid: String,
    val personality: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandVoiceScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val currentVoice by viewModel.brandVoice.collectAsStateWithLifecycle()

    var brandName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var targetAudience by remember { mutableStateOf("") }
    var tone by remember { mutableStateOf(Tone.LUXURY) }
    var wordsToUse by remember { mutableStateOf("") }
    var wordsToAvoid by remember { mutableStateOf("") }
    var personality by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

    var showToneMenu by remember { mutableStateOf(false) }

    val presets = listOf(
        VoicePreset(
            title = "✨ Luxe & Minimalist",
            brandName = "Aura Minimalist Living",
            description = "Thoughtfully engineered essentials for modern architecture and peaceful homes.",
            audience = "Design-conscious urban homeowners aged 28-50.",
            tone = Tone.LUXURY,
            wordsToUse = "timeless, tactile, serene, architectural, effortless",
            wordsToAvoid = "cheap, discount, bargain, noisy, clutter",
            personality = "Understated, refined, tranquil, confident"
        ),
        VoicePreset(
            title = "🚀 High-Growth Tech",
            brandName = "PulseFlow Automation",
            description = "Real-time AI workflows eliminating manual back-office drag for modern teams.",
            audience = "Founders, CTOs, and high-velocity engineering leaders.",
            tone = Tone.PROFESSIONAL,
            wordsToUse = "leverage, 10x velocity, seamless sync, unblock, mission-critical",
            wordsToAvoid = "magic, overnight fix, outdated, slow",
            personality = "Sharp, authoritative, pragmatic, forward-looking"
        ),
        VoicePreset(
            title = "🌿 Clean & Organic",
            brandName = "Botanica Wellness",
            description = "Clinically verified plant-based nutrition backed by third-party testing.",
            audience = "Active lifestyle enthusiasts and mindful holistic shoppers.",
            tone = Tone.FRIENDLY,
            wordsToUse = "nourish, pure, sustainable, vibrant, vitality",
            wordsToAvoid = "synthetic, chemical, miraculous, guilt",
            personality = "Warm, encouraging, transparent, grounded"
        ),
        VoicePreset(
            title = "⚡ Direct Response",
            brandName = "Apex Performance",
            description = "Precision athletic recovery gear built for competitive marathoners.",
            audience = "Dedicated endurance athletes seeking measurable performance gains.",
            tone = Tone.PERSUASIVE,
            wordsToUse = "proven, elite, guaranteed, edge, breakthrough",
            wordsToAvoid = "maybe, casual, average, generic",
            personality = "High-energy, relentless, factual, results-obsessed"
        )
    )

    LaunchedEffect(currentVoice) {
        brandName = currentVoice.brandName
        description = currentVoice.description
        targetAudience = currentVoice.targetAudience
        tone = currentVoice.tone
        wordsToUse = currentVoice.wordsToUse
        wordsToAvoid = currentVoice.wordsToAvoid
        personality = currentVoice.personality
        isActive = currentVoice.isActive
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("brand_voice_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Brand Voice & Identity",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Teach SellAI how your brand writes and communicates. All future generations will automatically adopt these guidelines.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Active Toggle Strip
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyanAccent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Auto-Inject Brand Voice", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (isActive) "Active in all generators" else "Currently disabled",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isActive) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = isActive,
                        onCheckedChange = { isActive = it },
                        modifier = Modifier.testTag("brand_voice_active_switch")
                    )
                }
            }
        }

        // Quick Preset Selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1-Click Industry Presets",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Select an archetype to auto-populate your vocabulary and guidelines:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        presets.forEach { preset ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                    .clickable {
                                        brandName = preset.brandName
                                        description = preset.description
                                        targetAudience = preset.audience
                                        tone = preset.tone
                                        wordsToUse = preset.wordsToUse
                                        wordsToAvoid = preset.wordsToAvoid
                                        personality = preset.personality
                                        viewModel.showToast("Loaded preset: ${preset.title}")
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = preset.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Configuration Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Brand / Company Name *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = brandName,
                        onValueChange = { brandName = it },
                        placeholder = { Text("e.g. LuxeGlow Skincare") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brand_name_field"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Brand Story & Mission", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("What is your core promise, mission, and why should customers trust you?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("brand_desc_field"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Audience Persona", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = targetAudience,
                        onValueChange = { targetAudience = it },
                        placeholder = { Text("e.g. Busy urban founders seeking effortless skincare") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brand_audience_field"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Default Tone of Voice", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        OutlinedTextField(
                            value = tone.displayName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showToneMenu = true }) {
                                    Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showToneMenu = true },
                            shape = RoundedCornerShape(8.dp)
                        )
                        DropdownMenu(
                            expanded = showToneMenu,
                            onDismissRequest = { showToneMenu = false }
                        ) {
                            Tone.values().forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t.displayName) },
                                    onClick = {
                                        tone = t
                                        showToneMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Preferred Vocabulary (Keywords to Favor)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = CyanAccent)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = wordsToUse,
                        onValueChange = { wordsToUse = it },
                        placeholder = { Text("e.g. radiance, effortless, clinically proven, mindful ritual") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brand_words_to_use_field"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Negative Vocabulary (Words & Jargon to Avoid)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = wordsToAvoid,
                        onValueChange = { wordsToAvoid = it },
                        placeholder = { Text("e.g. cheap, miracle cure, magic, hustle, hype") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brand_words_to_avoid_field"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Personality Description", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = personality,
                        onValueChange = { personality = it },
                        placeholder = { Text("e.g. Sophisticated, authoritative yet warm and witty") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brand_personality_field"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            viewModel.updateBrandVoice(
                                BrandVoice(
                                    brandName = brandName,
                                    description = description,
                                    targetAudience = targetAudience,
                                    tone = tone,
                                    wordsToUse = wordsToUse,
                                    wordsToAvoid = wordsToAvoid,
                                    personality = personality,
                                    isActive = isActive
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_brand_voice_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Apply Brand Guidelines", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
