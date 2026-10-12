package org.keepandroidopen.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class CountdownTest {

    private val zone = ZoneId.of("UTC")
    private val target = lockdownInstant(zone)

    @Test
    fun countsWholeDaysUntilLockdown() {
        val now = ZonedDateTime.of(2026, 12, 31, 0, 0, 0, 0, zone)
        val parts = countdownBetween(target, now)
        assertEquals(1, parts.days)
        assertEquals(0, parts.hours)
        assertEquals(0, parts.minutes)
        assertEquals(0, parts.seconds)
        assertFalse(parts.expired)
    }

    @Test
    fun splitsHoursMinutesSeconds() {
        val now = ZonedDateTime.of(2026, 12, 30, 21, 30, 15, 0, zone)
        val parts = countdownBetween(target, now)
        assertEquals(1, parts.days)
        assertEquals(2, parts.hours)
        assertEquals(29, parts.minutes)
        assertEquals(45, parts.seconds)
    }

    @Test
    fun targetInstantIsJanuaryFirst2027() {
        assertEquals(2027, target.year)
        assertEquals(1, target.monthValue)
        assertEquals(1, target.dayOfMonth)
    }

    @Test
    fun isExpiredAfterTheDeadline() {
        val now = ZonedDateTime.of(2027, 1, 2, 0, 0, 0, 0, zone)
        val parts = countdownBetween(target, now)
        assertTrue(parts.expired)
        assertEquals(0, parts.days)
    }
}
