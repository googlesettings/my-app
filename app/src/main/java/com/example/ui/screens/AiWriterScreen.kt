package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.ContentLength
import com.example.model.ContentType
import com.example.model.Language
import com.example.model.Tone
import com.example.ui.components.AiGeneratingCard
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.SellAiViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiWriterScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val productInput by viewModel.writerProductInput.collectAsStateWithLifecycle()
    val contentType by viewModel.writerContentType.collectAsStateWithLifecycle()
    val targetAudience by viewModel.writerTargetAudience.collectAsStateWithLifecycle()
    val tone by viewModel.writerTone.collectAsStateWithLifecycle()
    val language by viewModel.writerLanguage.collectAsStateWithLifecycle()
    val length by viewModel.writerLength.collectAsStateWithLifecycle()
    val cta by viewModel.writerCta.collectAsStateWithLifecycle()
    val applyBrandVoice by viewModel.writerApplyBrandVoice.collectAsStateWithLifecycle()
    val isGenerating by viewModel.writerIsGenerating.collectAsStateWithLifecycle()
    val generatedContent by viewModel.writerGeneratedContent.collectAsStateWithLifecycle()
    val brandVoice by viewModel.brandVoice.collectAsStateWithLifecycle()

    var showAdvancedSettings by remember { mutableStateOf(false) }
    var showToneMenu by remember { mutableStateOf(false) }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var showToneChangeDialog by remember { mutableStateOf(false) }
    var showTranslateDialog by remember { mutableStateOf(false) }

    val wordCount = remember(generatedContent) {
        if (generatedContent.isBlank()) 0 else generatedContent.trim().split("\\s+".toRegex()).size
    }
    val readingTimeSeconds = remember(wordCount) {
        (wordCount / 3.3).toInt().coerceAtLeast(1)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("ai_writer_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Heading
        item {
            Column {
                Text(
                    text = "AI Copywriter Studio",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Engineered for maximum conversion rates across all channels.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Quick Content Type Selector Bar
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ContentType.values()) { type ->
                    val isSelected = contentType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.writerContentType.value = type },
                        label = { Text(type.displayName, fontSize = 12.sp) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("content_type_${type.name.lowercase()}")
                    )
                }
            }
        }

        // Main Input Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Product / Service / Offer *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = productInput,
                        onValueChange = { viewModel.writerProductInput.value = it },
                        placeholder = {
                            Text(
                                "Describe what you are selling, primary benefits, target customer, or special launch deal...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(105.dp)
                            .testTag("writer_product_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Advanced Persona Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showAdvancedSettings = !showAdvancedSettings }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tone, Audience & Brand Persona",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (showAdvancedSettings) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Advanced collapsible section
                    AnimatedVisibility(visible = showAdvancedSettings) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Target Audience
                            Column {
                                Text("Target Audience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = targetAudience,
                                    onValueChange = { viewModel.writerTargetAudience.value = it },
                                    placeholder = { Text("e.g. Remote tech workers, busy parents, e-commerce brands") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("writer_audience_input"),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }

                            // Tone & Language
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Tone of Voice", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box {
                                        OutlinedTextField(
                                            value = tone.displayName,
                                            onValueChange = {},
                                            readOnly = true,
                                            trailingIcon = {
                                                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.clickable { showToneMenu = true })
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { showToneMenu = true },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        DropdownMenu(expanded = showToneMenu, onDismissRequest = { showToneMenu = false }) {
                                            Tone.values().forEach { t ->
                                                DropdownMenuItem(
                                                    text = { Text(t.displayName) },
                                                    onClick = {
                                                        viewModel.writerTone.value = t
                                                        showToneMenu = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Language", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box {
                                        OutlinedTextField(
                                            value = language.displayName,
                                            onValueChange = {},
                                            readOnly = true,
                                            trailingIcon = {
                                                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.clickable { showLanguageMenu = true })
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { showLanguageMenu = true },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        DropdownMenu(expanded = showLanguageMenu, onDismissRequest = { showLanguageMenu = false }) {
                                            Language.values().forEach { lang ->
                                                DropdownMenuItem(
                                                    text = { Text(lang.displayName) },
                                                    onClick = {
                                                        viewModel.writerLanguage.value = lang
                                                        showLanguageMenu = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Length selector
                            Column {
                                Text("Length", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ContentLength.values().forEach { len ->
                                        val isSelected = length == len
                                        OutlinedButton(
                                            onClick = { viewModel.writerLength.value = len },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                            ),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = len.displayName,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }

                            // Call to Action
                            Column {
                                Text("Call To Action", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = cta,
                                    onValueChange = { viewModel.writerCta.value = it },
                                    placeholder = { Text("e.g. Claim 20% off today, Tap link in bio") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }

                            // Brand Voice Toggle Strip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Apply Brand Voice Memory", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = if (brandVoice.isActive && brandVoice.brandName.isNotBlank()) "${brandVoice.brandName} (${brandVoice.tone.displayName})" else "Voice inactive",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CyanAccent
                                    )
                                }
                                Switch(
                                    checked = applyBrandVoice,
                                    onCheckedChange = { viewModel.writerApplyBrandVoice.value = it },
                                    modifier = Modifier.testTag("writer_brand_voice_switch")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateWriterContent() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_with_sellai_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Generate with SellAI", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        // Loading Card
        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is synthesizing your ${contentType.displayName}...")
            }
        }

        // Generated Result Editor
        if (generatedContent.isNotBlank() && !isGenerating) {
            item {
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
                            Column {
                                Text(
                                    text = "Generated Output",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$wordCount words • ~$readingTimeSeconds sec read",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                CopyButton(
                                    textToCopy = generatedContent,
                                    onCopied = { viewModel.showToast("Copied to clipboard!") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { viewModel.saveCurrentWriterContent() },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(34.dp)
                                        .testTag("save_writer_result_btn")
                                ) {
                                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Save", fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = generatedContent,
                            onValueChange = { viewModel.writerGeneratedContent.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .testTag("generated_content_editor"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "1-Click AI Refinements",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.generateWriterContent() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("regenerate_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Regenerate", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { viewModel.improveWriterContent() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("improve_btn")
                            ) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(13.dp), tint = CyanAccent)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Enhance Hooks", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { viewModel.makeWriterShorter() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("make_shorter_btn")
                            ) {
                                Icon(imageVector = Icons.Default.UnfoldLess, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Make Shorter", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { viewModel.makeWriterLonger() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("make_longer_btn")
                            ) {
                                Icon(imageVector = Icons.Default.UnfoldMore, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Make Longer", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { showToneChangeDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("change_tone_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Change Tone", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = { showTranslateDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp).testTag("translate_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Language, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Translate", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showToneChangeDialog) {
        AlertDialog(
            onDismissRequest = { showToneChangeDialog = false },
            title = { Text("Select New Tone", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Tone.values().forEach { t ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.changeWriterTone(t)
                                    showToneChangeDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(t.displayName, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showToneChangeDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showTranslateDialog) {
        AlertDialog(
            onDismissRequest = { showTranslateDialog = false },
            title = { Text("Translate Content", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Language.values().forEach { l ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.translateWriterContent(l)
                                    showTranslateDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(l.displayName, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showTranslateDialog = false }) { Text("Cancel") }
            }
        )
    }
}
