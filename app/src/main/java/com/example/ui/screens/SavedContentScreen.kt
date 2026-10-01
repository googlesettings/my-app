package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SavedContentEntity
import com.example.model.Screen
import com.example.ui.components.CopyButton
import com.example.ui.components.SellAiEmptyState
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PinkAccent
import com.example.ui.viewmodel.SellAiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedContentScreen(
    viewModel: SellAiViewModel,
    modifier: Modifier = Modifier
) {
    val savedItems by viewModel.savedContentList.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var itemToDelete by remember { mutableStateOf<SavedContentEntity?>(null) }
    var activeDetailItem by remember { mutableStateOf<SavedContentEntity?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val filterOptions = listOf("All", "Favorites", "Social", "Advertising", "E-Commerce", "Email", "Hashtags")

    val filteredList = savedItems.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.content.contains(searchQuery, ignoreCase = true) ||
                item.contentType.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "All" -> true
            "Favorites" -> item.isFavorite
            "Social" -> item.platform.equals("TikTok", true) || item.platform.equals("Instagram", true) || item.contentType.contains("Social", true) || item.contentType.contains("Caption", true)
            "Advertising" -> item.platform.equals("Advertising", true) || item.contentType.contains("Ad", true)
            "E-Commerce" -> item.contentType.contains("Product", true) || item.platform.equals("E-Commerce", true)
            "Email" -> item.contentType.contains("Email", true)
            "Hashtags" -> item.contentType.contains("Hashtags", true)
            else -> true
        }

        matchesSearch && matchesFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("saved_content_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Campaign Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${savedItems.size} marketing assets saved locally in Room SQLite.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    if (savedItems.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                val bundle = buildString {
                                    append("# SellAI Exported Campaign Library\n\n")
                                    savedItems.forEach { item ->
                                        append("## ${item.title} (${item.contentType})\n")
                                        append("${item.content}\n\n---\n\n")
                                    }
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("SellAI All Campaigns", bundle)
                                clipboard.setPrimaryClip(clip)
                                viewModel.showToast("Exported all ${savedItems.size} campaigns to clipboard!")
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export", fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        TextButton(
                            onClick = { showClearAllDialog = true },
                            modifier = Modifier.height(34.dp).testTag("clear_all_saved_btn")
                        ) {
                            Text("Clear", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search your copy, hooks, ads or headlines...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear search", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("saved_search_field"),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
        }

        // Filter Chips Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }

        // List or Empty State
        if (filteredList.isEmpty()) {
            item {
                SellAiEmptyState(
                    title = if (searchQuery.isNotBlank() || selectedFilter != "All") "No Matching Campaigns" else "Library is Empty",
                    description = if (searchQuery.isNotBlank() || selectedFilter != "All")
                        "Try adjusting your search terms or clearing the current category filter."
                    else
                        "Generate high-converting marketing campaigns using the AI Writer, Ads, or Social Hub to save them here.",
                    icon = Icons.Default.BookmarkBorder,
                    actionLabel = if (searchQuery.isNotBlank() || selectedFilter != "All") "Clear Filters" else "Create First Campaign",
                    onActionClick = {
                        if (searchQuery.isNotBlank() || selectedFilter != "All") {
                            searchQuery = ""
                            selectedFilter = "All"
                        } else {
                            viewModel.navigateTo(Screen.AI_WRITER)
                        }
                    }
                )
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                SavedContentItemCard(
                    item = item,
                    onToggleFavorite = { viewModel.toggleFavorite(item) },
                    onDelete = { itemToDelete = item },
                    onOpen = { viewModel.openInWriter(item) },
                    onDetails = { activeDetailItem = item },
                    onCopied = { viewModel.showToast(it) }
                )
            }
        }
    }

    // Detail Modal Dialog for viewing complete formatted text
    activeDetailItem?.let { detail ->
        Dialog(onDismissRequest = { activeDetailItem = null }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(IndigoPrimary.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(detail.contentType, color = IndigoPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        IconButton(onClick = { activeDetailItem = null }, modifier = Modifier.size(28.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(detail.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        LazyColumn(modifier = Modifier.padding(12.dp)) {
                            item {
                                Text(detail.content, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                activeDetailItem = null
                                viewModel.openInWriter(detail)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open in Editor")
                        }

                        CopyButton(
                            textToCopy = detail.content,
                            onCopied = { viewModel.showToast("Copied to clipboard!") }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Campaign?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete '${item.title}' from your library?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSavedItem(item.id)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("Cancel") }
            }
        )
    }

    // Clear all dialog
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text("Clear Entire Library?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently remove all saved marketing content from your device.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllSavedContent()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SavedContentItemCard(
    item: SavedContentEntity,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onOpen: () -> Unit,
    onDetails: () -> Unit,
    onCopied: (String) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val formattedDate = remember(item.createdAt) { dateFormat.format(Date(item.createdAt)) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetails() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(IndigoPrimary.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.contentType,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = if (item.isFavorite) PinkAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete item",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpen,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit in Studio", fontSize = 11.sp)
                }

                CopyButton(
                    textToCopy = item.content,
                    onCopied = onCopied
                )
            }
        }
    }
}
