package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.theme.SellAiGradients
import com.example.ui.viewmodel.SellAiViewModel

data class OnboardingSlide(
    val icon: ImageVector,
    val iconColor: Color,
    val title: String,
    val subtitle: String,
    val highlightBadge: String
)

@Composable
fun OnboardingScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(0) }

    val slides = listOf(
        OnboardingSlide(
            icon = Icons.Default.AutoAwesome,
            iconColor = CyanAccent,
            title = "AI Marketing That Actually Sells",
            subtitle = "Generate high-converting Instagram captions, TikTok video scripts, product descriptions, Facebook ads and emails in seconds.",
            highlightBadge = "SMART CONTENT ENGINE"
        ),
        OnboardingSlide(
            icon = Icons.Default.Campaign,
            iconColor = PinkAccent,
            title = "14-in-1 Campaign Generator",
            subtitle = "Enter your product idea once and get a complete launch strategy: hooks, ad variations, email sequences, and a 7-day content calendar.",
            highlightBadge = "MULTI-PLATFORM LAUNCH"
        ),
        OnboardingSlide(
            icon = Icons.Default.RecordVoiceOver,
            iconColor = IndigoPrimary,
            title = "Custom Brand Voice Consistency",
            subtitle = "Define your brand tone, target audience, and preferred vocabulary. SellAI seamlessly maintains your distinctive identity everywhere.",
            highlightBadge = "VOICE MEMORY & SECURITY"
        )
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("onboarding_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(SellAiGradients.brandGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SellAI", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = { viewModel.navigateTo(Screen.DASHBOARD) }) {
                    Text("Skip to App", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Animated Slide Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_step"
            ) { step ->
                val slide = slides[step]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(slide.iconColor.copy(alpha = 0.12f))
                            .border(1.dp, slide.iconColor.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = slide.icon,
                            contentDescription = null,
                            tint = slide.iconColor,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = slide.highlightBadge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = slide.iconColor
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = slide.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            // Bottom Navigation & Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Step Indicator Dots
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    slides.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (currentStep == index) 20.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (currentStep == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (currentStep < slides.lastIndex) {
                    Button(
                        onClick = { currentStep++ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("onboarding_next_btn")
                    ) {
                        Text("Next", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { viewModel.navigateTo(Screen.REGISTER) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("onboarding_get_started_btn")
                    ) {
                        Text("Get Started Free", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.LOGIN) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("onboarding_sign_in_btn")
                ) {
                    Text("I already have an account", fontSize = 14.sp)
                }
            }
        }
    }
}
