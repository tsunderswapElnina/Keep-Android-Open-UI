package org.keepandroidopen.app.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.keepandroidopen.app.R

fun Context.openUrl(url: String) {
    try {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (e: ActivityNotFoundException) {
        toast(getString(R.string.toast_no_app))
    }
}

fun Context.sendEmail(
    to: String?,
    cc: String? = null,
    subject: String,
    body: String,
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, if (to.isNullOrBlank()) emptyArray() else arrayOf(to))
        if (!cc.isNullOrBlank()) putExtra(Intent.EXTRA_CC, arrayOf(cc))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    try {
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_send_email)))
    } catch (e: ActivityNotFoundException) {
        toast(getString(R.string.toast_no_email))
    }
}

fun Context.shareText(subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    startActivity(Intent.createChooser(intent, getString(R.string.chooser_share)))
}

fun Context.copyToClipboard(label: String, text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    toast(getString(R.string.toast_copied))
}

private fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
