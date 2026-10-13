package org.keepandroidopen.app.ui

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.keepandroidopen.app.R
import org.keepandroidopen.app.SettingsActivity
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.ui.screens.ActNowScreen
import org.keepandroidopen.app.ui.screens.CountdownScreen
import org.keepandroidopen.app.ui.screens.RolloutScreen
import org.keepandroidopen.app.util.openUrl

enum class KaoTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    Countdown(R.string.tab_countdown, Icons.Filled.Timer),
    Rollout(R.string.tab_rollout, Icons.Filled.Public),
    ActNow(R.string.tab_act_now, Icons.Filled.Campaign),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAndroidOpenApp() {
    var tab by rememberSaveable { mutableStateOf(KaoTab.Countdown) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(tab.labelRes)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    IconButton(onClick = { context.openUrl(KeepAndroidOpen.SITE_URL) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = stringResource(R.string.cd_open_site),
                        )
                    }
                    IconButton(onClick = {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.cd_settings),
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                KaoTab.entries.forEach { item ->
                    NavigationBarItem(
                        selected = tab == item,
                        onClick = { tab = item },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.labelRes)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        AnimatedContent(
            targetState = tab,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(120))
            },
            label = "tab-content",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { target ->
            when (target) {
                KaoTab.Countdown -> CountdownScreen()
                KaoTab.Rollout -> RolloutScreen()
                KaoTab.ActNow -> ActNowScreen()
            }
        }
    }
}
