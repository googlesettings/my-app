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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.SellAiGradients
import com.example.ui.viewmodel.SellAiViewModel

@Composable
fun CreateHubScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("create_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Content Studio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Select a dedicated marketing format", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyanAccent.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("6 Native Generators", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        }
                    }
                }
            }
        }

        // Section: Primary Generator Tools
        item {
            Text(
                text = "Marketing Generators",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. AI Writer
                ToolHubCard(
                    title = "AI Writer & Copy Editor",
                    description = "Instagram captions, Facebook posts, WhatsApp messages, LinkedIn posts & marketing hooks.",
                    badge = "General Purpose",
                    icon = Icons.Default.AutoAwesome,
                    color = IndigoPrimary,
                    onClick = { viewModel.navigateTo(Screen.AI_WRITER) }
                )

                // 2. Product Description
                ToolHubCard(
                    title = "Product Description Generator",
                    description = "Short & full e-commerce copy, bulleted key benefits, technical features & strong checkout CTAs.",
                    badge = "E-Commerce",
                    icon = Icons.Default.Description,
                    color = CyanAccent,
                    onClick = { viewModel.navigateTo(Screen.PRODUCT_DESCRIPTION) }
                )

                // 3. Social Media
                ToolHubCard(
                    title = "Social Media & TikTok Scripts",
                    description = "TikTok scripts with timing cues, Instagram reels hooks, carousel breakdowns and hashtags.",
                    badge = "Short-form Video",
                    icon = Icons.Default.Videocam,
                    color = Color(0xFF8B5CF6),
                    onClick = { viewModel.navigateTo(Screen.SOCIAL_MEDIA) }
                )

                // 4. Ads
                ToolHubCard(
                    title = "Ad Campaign Generator",
                    description = "High-CTR variations for Facebook, Instagram, Google Ads & TikTok with headlines, primary text & CTAs.",
                    badge = "Paid Acquisition",
                    icon = Icons.Default.Campaign,
                    color = PinkAccent,
                    onClick = { viewModel.navigateTo(Screen.ADS) }
                )

                // 5. Email
                ToolHubCard(
                    title = "Marketing Email Writer",
                    description = "Launch announcements, scarcity closes, A/B test subject lines, and preview text.",
                    badge = "Retention",
                    icon = Icons.Default.Email,
                    color = Color(0xFFF59E0B),
                    onClick = { viewModel.navigateTo(Screen.EMAIL) }
                )

                // 6. Hashtags
                ToolHubCard(
                    title = "Hashtag Research Engine",
                    description = "Categorized high reach, medium competition, and niche targeted hashtag sets.",
                    badge = "Organic Reach",
                    icon = Icons.Default.Tag,
                    color = EmeraldSuccess,
                    onClick = { viewModel.navigateTo(Screen.HASHTAGS) }
                )
            }
        }
    }
}

@Composable
fun ToolHubCard(
    title: String,
    description: String,
    badge: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f))
                    .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = badge, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = color)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
