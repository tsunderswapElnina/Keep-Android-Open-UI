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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.keepandroidopen.app.R
import org.keepandroidopen.app.data.Contact
import org.keepandroidopen.app.data.ContactSeed
import org.keepandroidopen.app.data.Content
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.ui.components.KaoButton
import org.keepandroidopen.app.ui.components.KaoCard
import org.keepandroidopen.app.ui.components.KaoChip
import org.keepandroidopen.app.ui.components.KaoOutlinedButton
import org.keepandroidopen.app.ui.components.ScreenPadding
import org.keepandroidopen.app.ui.components.SectionHeader
import org.keepandroidopen.app.util.copyToClipboard
import org.keepandroidopen.app.util.openUrl
import org.keepandroidopen.app.util.sendEmail
import org.keepandroidopen.app.util.shareText

@Composable
private fun buildContacts(
    regionsRes: Int,
    namesRes: Int,
    detailsRes: Int,
    seeds: List<ContactSeed>,
): List<Contact> {
    val regions = stringArrayResource(regionsRes)
    val names = stringArrayResource(namesRes)
    val details = stringArrayResource(detailsRes)
    return regions.indices.map { i ->
        Contact(
            region = regions[i],
            name = names[i],
            detail = details[i],
            url = seeds.getOrNull(i)?.url,
            email = seeds.getOrNull(i)?.email,
        )
    }
}

@Composable
fun ActNowScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardLabel = stringResource(R.string.act_clipboard_label)
    val subject = stringResource(R.string.letter_subject)
    val genericLetter = stringResource(
        R.string.letter_template,
        stringResource(R.string.letter_region_generic),
        KeepAndroidOpen.VICTORY_EMAIL,
    )
    val usContacts = buildContacts(
        R.array.us_contact_regions,
        R.array.us_contact_names,
        R.array.us_contact_details,
        Content.usContactSeeds,
    )
    val worldContacts = buildContacts(
        R.array.world_contact_regions,
        R.array.world_contact_names,
        R.array.world_contact_details,
        Content.worldContactSeeds,
    )
    val advocacy = buildContacts(
        R.array.advocacy_regions,
        R.array.advocacy_names,
        R.array.advocacy_details,
        Content.advocacySeeds,
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = ScreenPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { SectionHeader(stringResource(R.string.act_title)) }
        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    text = stringResource(R.string.act_intro, KeepAndroidOpen.VICTORY_EMAIL),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        item {
            KaoButton(
                onClick = {
                    context.sendEmail(
                        to = null,
                        cc = KeepAndroidOpen.VICTORY_EMAIL,
                        subject = subject,
                        body = genericLetter,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Email, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.act_open_composer))
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                KaoOutlinedButton(
                    onClick = {
                        context.copyToClipboard(
                            clipboardLabel,
                            "$subject\n\n$genericLetter",
                        )
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.act_copy))
                }
                KaoOutlinedButton(
                    onClick = {
                        context.shareText(subject, "$subject\n\n$genericLetter")
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.act_share))
                }
            }
        }

        item { SectionHeader(stringResource(R.string.act_us_title)) }
        item {
            Text(
                text = stringResource(R.string.act_us_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(usContacts) { contact -> ContactCard(contact, subject, context) }

        item { SectionHeader(stringResource(R.string.act_world_title)) }
        items(worldContacts) { contact -> ContactCard(contact, subject, context) }

        item { SectionHeader(stringResource(R.string.act_groups_title)) }
        items(advocacy) { contact -> ContactCard(contact, subject, context) }

        item {
            KaoCard(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                Text(
                    text = stringResource(R.string.act_more_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = stringResource(R.string.act_more_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
        item {
            KaoButton(
                onClick = { context.openUrl(KeepAndroidOpen.ACT_URL) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.act_all_actions))
            }
        }
    }
}

@Composable
private fun ContactCard(contact: Contact, subject: String, context: Context) {
    val letter = stringResource(
        R.string.letter_template,
        contact.region,
        KeepAndroidOpen.VICTORY_EMAIL,
    )
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
                KaoButton(
                    onClick = {
                        context.sendEmail(
                            to = email,
                            cc = KeepAndroidOpen.VICTORY_EMAIL,
                            subject = subject,
                            body = letter,
                        )
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.Email, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.action_email))
                }
            }
            contact.url?.let { url ->
                KaoOutlinedButton(
                    onClick = { context.openUrl(url) },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.action_open))
                }
            }
        }
    }
}
