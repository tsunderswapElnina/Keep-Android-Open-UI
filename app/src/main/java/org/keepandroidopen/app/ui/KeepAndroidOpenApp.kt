package org.keepandroidopen.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
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
import org.keepandroidopen.app.data.KeepAndroidOpen
import org.keepandroidopen.app.ui.screens.ActNowScreen
import org.keepandroidopen.app.ui.screens.CountdownScreen
import org.keepandroidopen.app.ui.screens.RolloutScreen
import org.keepandroidopen.app.util.openUrl

enum class KaoTab(val label: String, val icon: ImageVector) {
    Countdown("Countdown", Icons.Filled.Timer),
    Rollout("The Rollout", Icons.Filled.Public),
    ActNow("Act Now", Icons.Filled.Campaign),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAndroidOpenApp() {
    var tab by rememberSaveable { mutableStateOf(KaoTab.Countdown) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tab.label) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    IconButton(onClick = { context.openUrl(KeepAndroidOpen.SITE_URL) }) {
                        Icon(
                            imageVector = Icons.Filled.OpenInNew,
                            contentDescription = "Open keepandroidopen.org",
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
                        label = { Text(item.label) },
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
