package org.example.roundapp.core.designsystem.components.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.example.roundapp.core.designsystem.resources.Res
import org.example.roundapp.core.designsystem.resources.round_app_logo_trans
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(Res.drawable.round_app_logo_trans),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(150.dp)
    )
}
