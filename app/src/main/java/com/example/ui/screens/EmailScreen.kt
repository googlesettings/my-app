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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AiGeneratingCard
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.SellAiViewModel

@Composable
fun EmailScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val product by viewModel.emailProduct.collectAsStateWithLifecycle()
    val audience by viewModel.emailAudience.collectAsStateWithLifecycle()
    val goal by viewModel.emailGoal.collectAsStateWithLifecycle()
    val result by viewModel.emailResult.collectAsStateWithLifecycle()
    val isGenerating by viewModel.emailIsGenerating.collectAsStateWithLifecycle()
    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()

    val goals = listOf(
        "Product Launch",
        "Flash Sale / Promo",
        "Cold B2B Outreach",
        "Weekly Newsletter",
        "Abandoned Cart Recovery",
        "Welcome Onboarding Series"
    )
    var showGoalMenu by remember { mutableStateOf(false) }
    var viewModeTab by remember { mutableIntStateOf(0) } // 0 = Copy View, 1 = Inbox Preview

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("email_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Email Marketing Copywriter",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "High-open subject lines, captivating preview text, and direct-response body copy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Inputs Card
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
                        Text("Campaign Scope", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = {
                                viewModel.emailProduct.value = "VIP Fall Collection: 30% off cashmere knit sweaters with free 2-day delivery"
                                viewModel.emailAudience.value = "VIP newsletter subscribers and past customers"
                                viewModel.emailGoal.value = "Flash Sale / Promo"
                            }
                        ) {
                            Text("Fill Sample", fontSize = 11.sp, color = IndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = product,
                        onValueChange = { viewModel.emailProduct.value = it },
                        placeholder = { Text("e.g. VIP Fall drop: 30% off cashmere knit sweaters with free delivery...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .testTag("email_product_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Subscriber Audience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = audience,
                                onValueChange = { viewModel.emailAudience.value = it },
                                placeholder = { Text("VIP subscribers") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("email_audience_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1.2f)) {
                            Text("Email Goal", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
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
                                                viewModel.emailGoal.value = g
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
                        onClick = { viewModel.generateEmail() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_email_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Full Email Campaign", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is crafting high-converting email copy and subject lines...")
            }
        }

        // Email Output
        result?.let { emailRes ->
            if (!isGenerating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Email Campaign Ready",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

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
                                Text("Inbox Mockup", fontSize = 11.sp, fontWeight = if (viewModeTab == 1) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }

                if (viewModeTab == 0) {
                    // Copy View
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
                                    Text("Campaign Content", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Row {
                                        CopyButton(
                                            textToCopy = "Subject: ${emailRes.subject}\nPre-header: ${emailRes.previewText}\n\n${emailRes.body}\n\nCTA: ${emailRes.cta}",
                                            onCopied = { viewModel.showToast(it) }
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Button(
                                            onClick = { viewModel.saveEmailContent() },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Save", fontSize = 11.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Subject Lines
                                Text("Subject Lines (A/B Test):", style = MaterialTheme.typography.labelSmall, color = CyanAccent, fontWeight = FontWeight.Bold)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Option A: ${emailRes.subject}", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Option B: ${emailRes.subjectAlt}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Preview Text (Pre-header):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Text(emailRes.previewText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Email Body:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(emailRes.body, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("CTA Button: ", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelSmall)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(IndigoPrimary)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(emailRes.cta, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Inbox Mockup View
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Email Client Window Bar
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldSuccess))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Inbox • Message Preview", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Spacer(modifier = Modifier.height(10.dp))

                                // Header info
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(IndigoPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(userAccount.name.take(1).uppercase(), color = IndigoPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("${userAccount.name} <${userAccount.email}>", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                        Text("to customer@domain.com", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(emailRes.subject, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(emailRes.previewText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(emailRes.body, style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { viewModel.showToast("CTA Button clicked: ${emailRes.cta}") },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text(emailRes.cta, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
