package org.keepandroidopen.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.keepandroidopen.app.data.Content
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.data.TimelineEvent
import org.keepandroidopen.app.data.VerificationPath
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.KaoChip
import org.keepandroidopen.app.ui.components.LabeledRow
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.util.openUrl

@Composable
fun RolloutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = ScreenPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            SectionHeader("What is Android Developer Verification?")
        }
        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    text = Content.ROLLOUT_SUMMARY,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        item {
            KaoCard {
                Text(
                    text = "What it really means",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = Content.WHAT_IT_MEANS,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item { SectionHeader("The rollout timeline") }
        items(Content.timeline) { event -> TimelineCard(event) }

        item { SectionHeader("Choose your verification path") }
        items(Content.paths) { path -> PathCard(path) }

        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                Text(
                    text = "The \u201cadvanced flow\u201d is not a solution",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = Content.ADVANCED_FLOW_NOTE,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }

        item {
            Button(
                onClick = { context.openUrl(KeepAndroidOpen.VERIFICATION_DOC_URL) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Google's official developer page")
            }
        }
        item {
            Button(
                onClick = { context.openUrl(KeepAndroidOpen.OPEN_LETTER_URL) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Read the open letter")
            }
        }
    }
}

@Composable
private fun TimelineCard(event: TimelineEvent) {
    KaoCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            KaoChip(text = event.date)
        }
        Text(
            text = event.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = event.detail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PathCard(path: VerificationPath) {
    KaoCard {
        Text(
            text = path.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        LabeledRow("Identity", path.identityRequired)
        LabeledRow("Distribution", path.distribution)
        LabeledRow("User experience", path.userExperience)
        LabeledRow("Best for", path.bestFor)
    }
}
