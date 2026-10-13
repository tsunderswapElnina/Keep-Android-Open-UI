package org.keepandroidopen.app.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import org.keepandroidopen.app.R
import org.keepandroidopen.app.settings.Accent
import org.keepandroidopen.app.settings.AppSettings
import org.keepandroidopen.app.settings.LanguageSettings
import org.keepandroidopen.app.settings.SupportedLocales
import org.keepandroidopen.app.settings.ThemeMode
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.ui.theme.md_light_primary
import java.util.Locale

private fun localeLabel(tag: String): String {
    val locale = Locale.forLanguageTag(tag)
    val name = locale.getDisplayName(Locale.getDefault())
    return name.replaceFirstChar { it.titlecase(Locale.getDefault()) }
}

private fun accentPreview(accent: Accent): Color =
    if (accent == Accent.DEFAULT) {
        md_light_primary
    } else {
        Color(AndroidColor.HSVToColor(floatArrayOf(accent.hue, 0.72f, 0.82f)))
    }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }
    var currentTag by remember { mutableStateOf(LanguageSettings.currentTag()) }
    val version = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull().orEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionHeader(stringResource(R.string.settings_language_title))
            KaoCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLanguageDialog = true },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Language, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentTag.isBlank()) {
                                stringResource(R.string.settings_system_default)
                            } else {
                                localeLabel(currentTag)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = stringResource(R.string.settings_language_summary),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            SectionHeader(stringResource(R.string.settings_theme_title))
            KaoCard {
                Text(
                    text = stringResource(R.string.settings_theme_summary),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                ThemeMode.entries.forEach { mode ->
                    val label = stringResource(
                        when (mode) {
                            ThemeMode.SYSTEM -> R.string.theme_system
                            ThemeMode.LIGHT -> R.string.theme_light
                            ThemeMode.DARK -> R.string.theme_dark
                            ThemeMode.AMOLED -> R.string.theme_amoled
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = settings.themeMode == mode,
                                role = Role.RadioButton,
                                onClick = { onSettingsChange(settings.copy(themeMode = mode)) },
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = settings.themeMode == mode,
                            onClick = { onSettingsChange(settings.copy(themeMode = mode)) },
                        )
                        Text(text = label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            SectionHeader(stringResource(R.string.settings_accent_title))
            KaoCard {
                Text(
                    text = stringResource(R.string.settings_accent_summary),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Accent.entries.forEach { accent ->
                        val selected = settings.accent == accent
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(accentPreview(accent), CircleShape)
                                .border(
                                    width = if (selected) 3.dp else 1.dp,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant
                                    },
                                    shape = CircleShape,
                                )
                                .clickable { onSettingsChange(settings.copy(accent = accent)) },
                            contentAlignment = Alignment.Center,
                        ) {
                            if (selected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
                if (settings.accent != Accent.DEFAULT) {
                    Text(
                        text = stringResource(R.string.settings_colorblendr_warning),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            SectionHeader(stringResource(R.string.settings_about_title))
            KaoCard {
                Text(
                    text = stringResource(R.string.settings_version, version),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.settings_open_source),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentTag = currentTag,
            onDismiss = { showLanguageDialog = false },
            onSelect = { tag ->
                currentTag = tag
                LanguageSettings.apply(tag)
                showLanguageDialog = false
            },
        )
    }
}

@Composable
private fun LanguagePickerDialog(
    currentTag: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val systemDefault = stringResource(R.string.settings_system_default)
    val all = remember {
        buildList {
            add("" to systemDefault)
            SupportedLocales.tags.forEach { tag -> add(tag to localeLabel(tag)) }
        }
    }
    val filtered = if (query.isBlank()) {
        all
    } else {
        val q = query.trim().lowercase(Locale.getDefault())
        listOf(all.first()) + all.drop(1).filter {
            it.first.lowercase(Locale.getDefault()).contains(q) ||
                it.second.lowercase(Locale.getDefault()).contains(q)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_language_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.settings_language_search)) },
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(360.dp)) {
                    items(filtered, key = { it.first }) { (tag, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = currentTag == tag,
                                    role = Role.RadioButton,
                                    onClick = { onSelect(tag) },
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = currentTag == tag,
                                onClick = { onSelect(tag) },
                            )
                            Text(text = label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
    )
}
