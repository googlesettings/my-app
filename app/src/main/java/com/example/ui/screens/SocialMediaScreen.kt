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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AiGeneratingCard
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.viewmodel.SellAiViewModel

@Composable
fun SocialMediaScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val platforms = listOf("TikTok", "Instagram", "Facebook", "LinkedIn", "X")
    val currentPlatform by viewModel.socialPlatform.collectAsStateWithLifecycle()
    val topic by viewModel.socialTopic.collectAsStateWithLifecycle()
    val audience by viewModel.socialAudience.collectAsStateWithLifecycle()
    val isGenerating by viewModel.socialIsGenerating.collectAsStateWithLifecycle()
    val tiktokRes by viewModel.tiktokResult.collectAsStateWithLifecycle()
    val instaRes by viewModel.instagramResult.collectAsStateWithLifecycle()
    val genericRes by viewModel.genericSocialResult.collectAsStateWithLifecycle()

    val selectedIndex = platforms.indexOf(currentPlatform).coerceAtLeast(0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("social_media_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Social Media Hub",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Engineered viral hooks, full camera-direction scripts, and platform-native captions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Platform Tab Bar
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            ) {
                platforms.forEachIndexed { index, name ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { viewModel.socialPlatform.value = name },
                        text = {
                            Text(
                                text = name,
                                fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("social_tab_${name.lowercase()}")
                    )
                }
            }
        }

        // Form Card
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
                        Text("Post Topic & Angle *", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = {
                                if (currentPlatform == "TikTok") {
                                    viewModel.socialTopic.value = "Why most creators waste 5 hours a week editing videos manually (and the modern fix)"
                                    viewModel.socialAudience.value = "Content creators and video editors"
                                } else {
                                    viewModel.socialTopic.value = "Behind the scenes: Redesigning our entire SaaS product architecture from scratch"
                                    viewModel.socialAudience.value = "Founders and tech enthusiasts"
                                }
                            }
                        ) {
                            Text("Fill Sample", fontSize = 11.sp, color = IndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { viewModel.socialTopic.value = it },
                        placeholder = { Text("e.g. Unboxing our new wireless keyboard, 3 productivity hacks, customer story...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .testTag("social_topic_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Audience (Optional)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = audience,
                        onValueChange = { viewModel.socialAudience.value = it },
                        placeholder = { Text("e.g. Video editors, founders, busy parents") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("social_audience_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateSocialContent() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_social_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Generate $currentPlatform Content", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is building your high-retention $currentPlatform content...")
            }
        }

        // Platform Specific Output
        if (!isGenerating) {
            when (currentPlatform) {
                "TikTok" -> {
                    tiktokRes?.let { tt ->
                        item {
                            TikTokResultView(
                                result = tt,
                                onSave = {
                                    val full = "${tt.hook}\n\n${tt.scriptBeats.joinToString("\n")}\n\nCTA: ${tt.cta}\n\nCaption: ${tt.caption}\n${tt.hashtags.joinToString(" ")}"
                                    viewModel.saveSocialContent("TikTok Script: ${topic.take(20)}", full)
                                },
                                onCopy = { viewModel.showToast(it) }
                            )
                        }
                    }
                }
                "Instagram" -> {
                    instaRes?.let { ig ->
                        item {
                            InstagramResultView(
                                result = ig,
                                onSave = {
                                    val full = "${ig.hook}\n\n${ig.caption}\n\nCTA: ${ig.cta}\n\n${ig.hashtags.joinToString(" ")}"
                                    viewModel.saveSocialContent("Instagram Post: ${topic.take(20)}", full)
                                },
                                onCopy = { viewModel.showToast(it) }
                            )
                        }
                    }
                }
                else -> {
                    if (genericRes.isNotBlank()) {
                        item {
                            GenericSocialResultView(
                                platform = currentPlatform,
                                content = genericRes,
                                onSave = { viewModel.saveSocialContent("$currentPlatform Post", genericRes) },
                                onCopy = { viewModel.showToast(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TikTokResultView(
    result: com.example.model.TikTokScriptResult,
    onSave: () -> Unit,
    onCopy: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TikTok Video Script Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    CopyButton(
                        textToCopy = "${result.hook}\n\n${result.scriptBeats.joinToString("\n")}\n\nCTA: ${result.cta}\n\nCaption: ${result.caption}\n${result.hashtags.joinToString(" ")}",
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

            Spacer(modifier = Modifier.height(12.dp))

            // Hook
            Text("1. Scroll-Stopping Hook (0:00 - 0:03):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PinkAccent)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Text(result.hook, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Script Beats
            Text("2. Full Video Script & Camera Directions:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    result.scriptBeats.forEach { beat ->
                        Text(beat, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 3.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Call to action
            Text("3. Call To Action:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(result.cta, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))

            Spacer(modifier = Modifier.height(12.dp))

            // Caption & Hashtags
            Text("4. Ready-to-Publish Caption:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(result.caption, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(result.hashtags.joinToString(" "), color = CyanAccent, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun InstagramResultView(
    result: com.example.model.InstagramResult,
    onSave: () -> Unit,
    onCopy: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Instagram Post & Caption", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    CopyButton(
                        textToCopy = "${result.hook}\n\n${result.caption}\n\n${result.cta}\n\n${result.hashtags.joinToString(" ")}",
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

            Spacer(modifier = Modifier.height(12.dp))

            Text("Headline Hook:", style = MaterialTheme.typography.labelSmall, color = CyanAccent, fontWeight = FontWeight.Bold)
            Text(result.hook, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Text("Formatted Caption:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(result.caption, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(result.cta, fontWeight = FontWeight.Bold, color = IndigoPrimary, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(result.hashtags.joinToString(" "), color = CyanAccent, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun GenericSocialResultView(
    platform: String,
    content: String,
    onSave: () -> Unit,
    onCopy: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$platform Content Ready", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    CopyButton(textToCopy = content, onCopied = onCopy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(onClick = onSave, shape = RoundedCornerShape(8.dp), modifier = Modifier.height(34.dp)) {
                        Text("Save", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
