package org.keepandroidopen.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.keepandroidopen.app.ui.KeepAndroidOpenApp
import org.keepandroidopen.app.ui.theme.KeepAndroidOpenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeepAndroidOpenTheme {
                KeepAndroidOpenApp()
            }
        }
    }
}
