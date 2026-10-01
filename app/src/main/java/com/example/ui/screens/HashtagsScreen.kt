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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import com.example.ui.components.SellAiEmptyState
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.viewmodel.SellAiViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagsScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val topic by viewModel.hashTopic.collectAsStateWithLifecycle()
    val niche by viewModel.hashNiche.collectAsStateWithLifecycle()
    val result by viewModel.hashtagsResult.collectAsStateWithLifecycle()
    val isGenerating by viewModel.hashIsGenerating.collectAsStateWithLifecycle()

    val suggestions = listOf("Noise-Canceling Audio", "Clean Skincare", "SaaS Productivity", "Specialty Coffee")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("hashtags_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Smart Hashtag Generator",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Target high reach, medium competition, and laser-focused niche tags for maximum algorithmic visibility.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Input Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Product / Topic / Keyword *", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { viewModel.hashTopic.value = it },
                        placeholder = { Text("e.g. Handmade ceramics, keto snacks, vintage streetwear") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hashtag_topic_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        suggestions.forEach { s ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.hashTopic.value = s }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(s, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Specific Sub-Niche or Location (Optional)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = niche,
                        onValueChange = { viewModel.hashNiche.value = it },
                        placeholder = { Text("e.g. Austin Texas, sustainable living, minimalist decor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hashtag_niche_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateHashtags() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_hashtags_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Tag, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Targeted Hashtag Clusters", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is finding highest-reach and niche hashtags...")
            }
        }

        // Hashtags Breakdown
        if (result != null && !isGenerating) {
            val hashRes = result!!
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hashtag Clusters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row {
                        val allTags = (hashRes.highReach + hashRes.mediumCompetition + hashRes.niche).joinToString(" ")
                        CopyButton(
                            textToCopy = allTags,
                            onCopied = { viewModel.showToast(it) },
                            label = "Copy All (${hashRes.highReach.size + hashRes.mediumCompetition.size + hashRes.niche.size})"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(onClick = { viewModel.saveHashtags() }, shape = RoundedCornerShape(8.dp), modifier = Modifier.height(34.dp)) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save", fontSize = 11.sp)
                        }
                    }
                }
            }

            // 1. High Reach Hashtags
            item {
                HashtagCategoryCard(
                    title = "🔥 High Reach (>1M Posts)",
                    subtitle = "Broad exposure to top-of-funnel traffic",
                    tags = hashRes.highReach,
                    badgeColor = PinkAccent,
                    onCopy = { viewModel.showToast("High reach tags copied!") }
                )
            }

            // 2. Medium Competition
            item {
                HashtagCategoryCard(
                    title = "⚡ Medium Competition (100k - 1M Posts)",
                    subtitle = "Balanced volume with strong engagement potential",
                    tags = hashRes.mediumCompetition,
                    badgeColor = IndigoPrimary,
                    onCopy = { viewModel.showToast("Medium competition tags copied!") }
                )
            }

            // 3. Niche Targeted
            item {
                HashtagCategoryCard(
                    title = "🎯 Niche Targeted (<100k Posts)",
                    subtitle = "High conversion intent from passionate buyer communities",
                    tags = hashRes.niche,
                    badgeColor = EmeraldSuccess,
                    onCopy = { viewModel.showToast("Niche tags copied!") }
                )
            }
        } else if (result == null && !isGenerating) {
            item {
                SellAiEmptyState(
                    title = "Generate Optimized Hashtag Clusters",
                    description = "Enter your primary product keyword above to generate categorized sets of high reach, medium competition, and niche hashtags.",
                    icon = Icons.Default.Tag
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HashtagCategoryCard(
    title: String,
    subtitle: String,
    tags: List<String>,
    badgeColor: Color,
    onCopy: () -> Unit
) {
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
                Column {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                CopyButton(textToCopy = tags.joinToString(" "), onCopied = { onCopy() })
            }

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.1f))
                            .border(1.dp, badgeColor.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = badgeColor
                        )
                    }
                }
            }
        }
    }
}
