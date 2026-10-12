package org.keepandroidopen.app.data

import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Core campaign constants. The lockdown target matches the countdown used by
 * keepandroidopen.org's embeddable banner (January 1, 2027, 00:00 local time).
 */
object KeepAndroidOpen {
    const val SITE_URL = "https://keepandroidopen.org"
    const val FAQ_URL = "https://keepandroidopen.org/faq"
    const val ACT_URL = "https://keepandroidopen.org/cta"
    const val BANNER_URL = "https://keepandroidopen.org/banner"
    const val OPEN_LETTER_URL = "https://f-droid.org/en/2026/02/24/open-letter-opposing-developer-verification.html"
    const val VERIFICATION_DOC_URL = "https://developer.android.com/developer-verification"
    const val PETITION_URL = "https://www.change.org/p/stop-google-from-limiting-apk-file-usage/"
    const val TIPS_EMAIL = "tips@keepandroidopen.org"
    const val VICTORY_EMAIL = "victory@keepandroidopen.org"
}

/** The instant the countdown is counting down to (January 1, 2027, 00:00 local time). */
fun lockdownInstant(zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime =
    ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, zone)

/** The day the Keep Android Open campaign began publicising the lockdown. */
fun campaignStartInstant(zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime =
    ZonedDateTime.of(2025, 8, 25, 0, 0, 0, 0, zone)

data class CountdownParts(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val totalSeconds: Long,
    val expired: Boolean,
)

fun countdownBetween(target: ZonedDateTime, now: ZonedDateTime): CountdownParts {
    val totalSeconds = Duration.between(now, target).seconds
    val expired = totalSeconds <= 0
    val clamped = if (totalSeconds < 0) 0 else totalSeconds
    val days = clamped / 86_400
    val hours = (clamped % 86_400) / 3_600
    val minutes = (clamped % 3_600) / 60
    val seconds = clamped % 60
    return CountdownParts(days, hours, minutes, seconds, totalSeconds, expired)
}
