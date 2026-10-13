package org.keepandroidopen.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.keepandroidopen.app.settings.AppSettings
import org.keepandroidopen.app.settings.SettingsStore
import org.keepandroidopen.app.ui.screens.SettingsScreen
import org.keepandroidopen.app.ui.theme.KeepAndroidOpenTheme

class SettingsActivity : AppCompatActivity() {

    private var settings by mutableStateOf(AppSettings.DEFAULT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = SettingsStore.read(this)
        enableEdgeToEdge()
        setContent {
            KeepAndroidOpenTheme(
                themeMode = settings.themeMode,
                accent = settings.accent,
            ) {
                SettingsScreen(
                    settings = settings,
                    onSettingsChange = { updated ->
                        if (updated.themeMode != settings.themeMode) {
                            SettingsStore.writeTheme(this, updated.themeMode)
                        }
                        if (updated.accent != settings.accent) {
                            SettingsStore.writeAccent(this, updated.accent)
                        }
                        settings = updated
                    },
                    onBack = { finish() },
                )
            }
        }
    }
}
