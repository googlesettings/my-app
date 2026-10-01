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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
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
import com.example.model.Language
import com.example.model.Tone
import com.example.ui.components.AiGeneratingCard
import com.example.ui.components.CopyButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.SellAiViewModel

@Composable
fun ProductDescriptionScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.prodName.collectAsStateWithLifecycle()
    val category by viewModel.prodCategory.collectAsStateWithLifecycle()
    val features by viewModel.prodFeatures.collectAsStateWithLifecycle()
    val benefits by viewModel.prodBenefits.collectAsStateWithLifecycle()
    val price by viewModel.prodPrice.collectAsStateWithLifecycle()
    val audience by viewModel.prodAudience.collectAsStateWithLifecycle()
    val result by viewModel.prodResult.collectAsStateWithLifecycle()
    val isGenerating by viewModel.prodIsGenerating.collectAsStateWithLifecycle()

    var showCategoryMenu by remember { mutableStateOf(false) }

    val categories = listOf(
        "Electronics & Hardware",
        "Fashion & Apparel",
        "Beauty & Skincare",
        "Home & Living",
        "Health & Nutrition",
        "SaaS & Digital Tools",
        "Specialty Food & Drink"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("product_description_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Product Description Studio",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Transform raw specifications into persuasive listings formatted for Shopify, Amazon, and DTC stores.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Input Form Card
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
                        Text("Product Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(
                            onClick = {
                                viewModel.prodName.value = "Lumina Pro Magnetic Desk Lamp"
                                viewModel.prodCategory.value = "Home & Living"
                                viewModel.prodFeatures.value = "Touch dimming, 2700K-6500K color temp, CNC milled aluminum, 4000mAh battery"
                                viewModel.prodBenefits.value = "Zero eye strain, wireless clean aesthetic, 12 hours of battery life"
                                viewModel.prodPrice.value = "$89.00"
                                viewModel.prodAudience.value = "Designers and remote workers"
                            }
                        ) {
                            Text("Fill Sample", fontSize = 11.sp, color = IndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1.3f)) {
                            Text("Product Name *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = name,
                                onValueChange = { viewModel.prodName.value = it },
                                placeholder = { Text("e.g. Lumina Desk Lamp") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("prod_name_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Category", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                OutlinedTextField(
                                    value = category.take(13),
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { showCategoryMenu = true }) {
                                            Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showCategoryMenu = true },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                DropdownMenu(
                                    expanded = showCategoryMenu,
                                    onDismissRequest = { showCategoryMenu = false }
                                ) {
                                    categories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat) },
                                            onClick = {
                                                viewModel.prodCategory.value = cat
                                                showCategoryMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Product Features (Specs & Materials)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = features,
                        onValueChange = { viewModel.prodFeatures.value = it },
                        placeholder = { Text("e.g. CNC aluminum body, 4000mAh battery, wireless fast charging...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("prod_features_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Customer Benefits (Transformative Outcomes)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = benefits,
                        onValueChange = { viewModel.prodBenefits.value = it },
                        placeholder = { Text("e.g. Eliminates desk clutter, protects eyesight during late work sessions...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("prod_benefits_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Price Point", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = price,
                                onValueChange = { viewModel.prodPrice.value = it },
                                placeholder = { Text("$89.00") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("prod_price_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1.4f)) {
                            Text("Target Audience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = audience,
                                onValueChange = { viewModel.prodAudience.value = it },
                                placeholder = { Text("Creative professionals") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("prod_audience_input"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.generateProductDescription() },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_prod_desc_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Description", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        if (isGenerating) {
            item {
                AiGeneratingCard(text = "SellAI is structuring your e-commerce product listing...")
            }
        }

        // Structured Breakdown View
        result?.let { res ->
            if (!isGenerating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Formatted Listing Output",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            val fullMarkdown = buildString {
                                append("## ${res.shortDescription}\n\n")
                                append("${res.fullDescription}\n\n")
                                append("### Key Benefits:\n")
                                res.keyBenefits.forEach { append("- $it\n") }
                                append("\n### Features:\n")
                                res.features.forEach { append("- $it\n") }
                                append("\n**${res.cta}**")
                            }
                            CopyButton(
                                textToCopy = fullMarkdown,
                                onCopied = { viewModel.showToast(it) },
                                label = "Copy All"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = { viewModel.saveProductDescription() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("save_prod_desc_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save", fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Short Hook
                item {
                    SectionCard(
                        title = "1. Catchy Hook Description",
                        content = res.shortDescription,
                        onCopy = { viewModel.showToast("Hook copied!") }
                    )
                }

                // Full Story
                item {
                    SectionCard(
                        title = "2. Full Narrative Description",
                        content = res.fullDescription,
                        onCopy = { viewModel.showToast("Full description copied!") }
                    )
                }

                // Key Benefits
                item {
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
                                Text("3. Key Benefits", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                CopyButton(
                                    textToCopy = res.keyBenefits.joinToString("\n• ", "• "),
                                    onCopied = { viewModel.showToast("Benefits copied!") }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            res.keyBenefits.forEach { b ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(b, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

                // Features
                item {
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
                                Text("4. Technical Specifications", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                CopyButton(
                                    textToCopy = res.features.joinToString("\n• ", "• "),
                                    onCopied = { viewModel.showToast("Features copied!") }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            res.features.forEach { f ->
                                Text("• $f", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                }

                // Closing Call To Action
                item {
                    SectionCard(
                        title = "5. Conversion CTA",
                        content = res.cta,
                        onCopy = { viewModel.showToast("CTA copied!") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: String,
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
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                CopyButton(textToCopy = content, onCopied = { onCopy() })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodySmall)
        }
    }
}
