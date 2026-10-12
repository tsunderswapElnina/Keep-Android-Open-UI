package org.keepandroidopen.app.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.keepandroidopen.app.data.Contact
import org.keepandroidopen.app.data.Content
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.KaoChip
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.util.copyToClipboard
import org.keepandroidopen.app.util.openUrl
import org.keepandroidopen.app.util.sendEmail
import org.keepandroidopen.app.util.shareText

private const val SUBJECT = "Stop Google's Android Developer Verification lockdown"

@Composable
fun ActNowScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = ScreenPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { SectionHeader("Act now \u2014 make your voice heard") }
        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    text = "Regulators want to hear from individuals who are affected. Be polite " +
                        "and specific, write in an official language of the body you contact, ask " +
                        "for a written acknowledgement, and copy " + KeepAndroidOpen.VICTORY_EMAIL +
                        " so the campaign can track victories.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        item {
            Button(
                onClick = {
                    context.sendEmail(
                        to = null,
                        cc = KeepAndroidOpen.VICTORY_EMAIL,
                        subject = SUBJECT,
                        body = Content.letterTemplate(),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.Filled.Email, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Open the email composer")
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        context.copyToClipboard("Keep Android Open letter", Content.letterTemplate())
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Copy")
                }
                OutlinedButton(
                    onClick = {
                        context.shareText(SUBJECT, Content.letterTemplate())
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Share")
                }
            }
        }

        item { SectionHeader("United States \u2014 email the government") }
        item {
            Text(
                text = "The Department of Justice and the FTC take antitrust reports through online " +
                    "forms. Use them, and contact your own representative and senators directly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(Content.usContacts) { contact -> ContactCard(contact, context) }

        item { SectionHeader("Elsewhere in the world") }
        items(Content.worldContacts) { contact -> ContactCard(contact, context) }

        item { SectionHeader("Consumer & digital rights groups") }
        items(Content.advocacyGroups) { contact -> ContactCard(contact, context) }

        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                Text(
                    text = "More ways to help",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = "\u2022 Install F-Droid on your devices.\n" +
                        "\u2022 Answer Google's developer verification survey.\n" +
                        "\u2022 Post on social media and link to keepandroidopen.org.\n" +
                        "\u2022 Sign the change.org petition.\n" +
                        "\u2022 Add the countdown banner to your website.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
        item {
            Button(
                onClick = { context.openUrl(KeepAndroidOpen.ACT_URL) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("All actions at keepandroidopen.org")
            }
        }
    }
}

@Composable
private fun ContactCard(contact: Contact, context: Context) {
    KaoCard {
        KaoChip(text = contact.region)
        Text(
            text = contact.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = contact.detail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        contact.email?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            contact.email?.let { email ->
                Button(
                    onClick = {
                        context.sendEmail(
                            to = email,
                            cc = KeepAndroidOpen.VICTORY_EMAIL,
                            subject = SUBJECT,
                            body = Content.letterTemplate(contact.region),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(Icons.Filled.Email, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Email")
                }
            }
            contact.url?.let { url ->
                OutlinedButton(
                    onClick = { context.openUrl(url) },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(Icons.Filled.OpenInNew, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Open")
                }
            }
        }
    }
}
