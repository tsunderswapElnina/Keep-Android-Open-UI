package org.keepandroidopen.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.keepandroidopen.app.R
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.ui.components.KaoButton
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.KaoChip
import org.keepandroidopen.app.ui.components.LabeledRow
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.util.openUrl

@Composable
fun RolloutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dates = stringArrayResource(R.array.timeline_dates)
    val titles = stringArrayResource(R.array.timeline_titles)
    val details = stringArrayResource(R.array.timeline_details)
    val pathNames = stringArrayResource(R.array.path_names)
    val pathIdentity = stringArrayResource(R.array.path_identity)
    val pathDistribution = stringArrayResource(R.array.path_distribution)
    val pathUx = stringArrayResource(R.array.path_ux)
    val pathBestFor = stringArrayResource(R.array.path_best_for)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = ScreenPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { SectionHeader(stringResource(R.string.rollout_title)) }
        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    text = stringResource(R.string.rollout_summary),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        item {
            KaoCard {
                Text(
                    text = stringResource(R.string.rollout_what_it_means_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.rollout_what_it_means),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item { SectionHeader(stringResource(R.string.rollout_timeline_title)) }
        itemsIndexed(dates.indices.toList()) { index, _ ->
            TimelineCard(dates[index], titles[index], details[index])
        }

        item { SectionHeader(stringResource(R.string.rollout_paths_title)) }
        itemsIndexed(pathNames.indices.toList()) { index, _ ->
            PathCard(
                name = pathNames[index],
                identity = pathIdentity[index],
                distribution = pathDistribution[index],
                ux = pathUx[index],
                bestFor = pathBestFor[index],
            )
        }

        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                Text(
                    text = stringResource(R.string.rollout_advanced_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = stringResource(R.string.rollout_advanced_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }

        item {
            KaoButton(
                onClick = { context.openUrl(KeepAndroidOpen.VERIFICATION_DOC_URL) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.rollout_dev_page))
            }
        }
        item {
            KaoButton(
                onClick = { context.openUrl(KeepAndroidOpen.OPEN_LETTER_URL) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.rollout_open_letter))
            }
        }
    }
}

@Composable
private fun TimelineCard(date: String, title: String, detail: String) {
    KaoCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            KaoChip(text = date)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PathCard(
    name: String,
    identity: String,
    distribution: String,
    ux: String,
    bestFor: String,
) {
    KaoCard {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        LabeledRow(stringResource(R.string.label_identity), identity)
        LabeledRow(stringResource(R.string.label_distribution), distribution)
        LabeledRow(stringResource(R.string.label_ux), ux)
        LabeledRow(stringResource(R.string.label_best_for), bestFor)
    }
}
