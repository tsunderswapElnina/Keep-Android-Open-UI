package org.keepandroidopen.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.keepandroidopen.app.R
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.data.campaignStartInstant
import org.keepandroidopen.app.data.countdownBetween
import org.keepandroidopen.app.data.lockdownInstant
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.util.openUrl
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CountdownScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val zone = remember { ZoneId.systemDefault() }
    val target = remember { lockdownInstant(zone) }
    val start = remember { campaignStartInstant(zone) }

    var now by remember { mutableStateOf(ZonedDateTime.now(zone)) }
    LaunchedEffect(Unit) {
        while (true) {
            now = ZonedDateTime.now(zone)
            delay(1000L)
        }
    }

    val parts = countdownBetween(target, now)
    val total = remember { Duration.between(start, target).seconds.toFloat().coerceAtLeast(1f) }
    val elapsed = Duration.between(start, now).seconds.toFloat()
    val fraction = (elapsed / total).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeroCountdownCard(
            days = parts.days,
            hours = parts.hours,
            minutes = parts.minutes,
            seconds = parts.seconds,
            expired = parts.expired,
        )

        KaoCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.countdown_campaign),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${(fraction * 100).roundToInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            LinearWavyProgressIndicator(
                progress = { fraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )
            Text(
                text = stringResource(R.string.countdown_elapsed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SectionHeader(stringResource(R.string.countdown_what_means))
        KaoCard {
            Text(
                text = stringResource(R.string.rollout_summary),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { context.openUrl(KeepAndroidOpen.FAQ_URL) },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.countdown_read_faq))
            }
            Button(
                onClick = { context.openUrl(KeepAndroidOpen.SITE_URL) },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.Filled.Schedule, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.countdown_campaign_btn))
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.countdown_footnote),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HeroCountdownCard(
    days: Long,
    hours: Long,
    minutes: Long,
    seconds: Long,
    expired: Boolean,
) {
    KaoCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
        Text(
            text = stringResource(
                if (expired) R.string.countdown_expired else R.string.countdown_running
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            letterSpacing = 1.5.sp,
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = days.toString(),
                fontSize = 108.sp,
                lineHeight = 112.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.unit_days),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 18.dp),
            )
        }
        if (!expired) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TimePill(hours, stringResource(R.string.unit_hours), Modifier.weight(1f))
                TimePill(minutes, stringResource(R.string.unit_minutes), Modifier.weight(1f))
                TimePill(seconds, stringResource(R.string.unit_seconds), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TimePill(value: Long, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format(Locale.US, "%02d", value),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
