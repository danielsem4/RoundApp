package org.example.roundapp.core.designsystem.components.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP,
}

private fun resolveDeviceConfiguration(maxWidth: Dp, maxHeight: Dp): DeviceConfiguration {
    val isLandscape = maxWidth > maxHeight
    return when {
        maxWidth >= 1200.dp -> DeviceConfiguration.DESKTOP
        maxWidth >= 840.dp && isLandscape -> DeviceConfiguration.TABLET_LANDSCAPE
        maxWidth >= 600.dp -> DeviceConfiguration.TABLET_PORTRAIT
        isLandscape -> DeviceConfiguration.MOBILE_LANDSCAPE
        else -> DeviceConfiguration.MOBILE_PORTRAIT
    }
}

@Composable
fun AppFormLayout(
    headerText: String,
    logo: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when (resolveDeviceConfiguration(maxWidth, maxHeight)) {
            DeviceConfiguration.MOBILE_PORTRAIT -> MobilePortrait(
                logo = logo,
                headerText = headerText,
                errorText = errorText,
                formContent = formContent,
            )
            DeviceConfiguration.MOBILE_LANDSCAPE -> MobileLandscape(
                logo = logo,
                headerText = headerText,
                errorText = errorText,
                formContent = formContent,
            )
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.DESKTOP -> CenteredCard(
                logo = logo,
                headerText = headerText,
                errorText = errorText,
                formContent = formContent,
            )
        }
    }
}

@Composable
private fun MobilePortrait(
    logo: @Composable () -> Unit,
    headerText: String,
    errorText: String?,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        logo()
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                AuthHeaderSection(headerText = headerText, errorText = errorText)
                Spacer(modifier = Modifier.height(24.dp))
                formContent()
            }
        }
    }
}

@Composable
private fun MobileLandscape(
    logo: @Composable () -> Unit,
    headerText: String,
    errorText: String?,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            logo()
            Spacer(modifier = Modifier.height(16.dp))
            AuthHeaderSection(headerText = headerText, errorText = errorText)
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                formContent()
            }
        }
    }
}

@Composable
private fun CenteredCard(
    logo: @Composable () -> Unit,
    headerText: String,
    errorText: String?,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        logo()
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(32.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
            ) {
                AuthHeaderSection(headerText = headerText, errorText = errorText)
                Spacer(modifier = Modifier.height(24.dp))
                formContent()
            }
        }
    }
}

@Composable
private fun ColumnScope.AuthHeaderSection(
    headerText: String,
    errorText: String?,
) {
    Text(
        text = headerText,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    AnimatedVisibility(visible = errorText != null) {
        if (errorText != null) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
