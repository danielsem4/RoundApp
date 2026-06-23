package org.example.roundapp.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Emphasis levels applied on top of the theme's `onSurface` color (Material convention). */
private const val SecondaryEmphasisAlpha = 0.6f
private const val DisabledEmphasisAlpha = 0.38f

/**
 * Universal, fully configurable input field for the whole app.
 *
 * Handles both plain text and secure (password) input via [isSecure], and exposes
 * optional start ([leadingIcon]) and end ([trailingIcon]) icons with per-icon tint and
 * click handler. Sizing knobs ([iconSize], [shape]) let callers design the field as they need.
 */
@Composable
fun AppInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    // labels
    title: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    // input type
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    isSecure: Boolean = false,
    isContentVisible: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
    // start icon
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    leadingIconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryEmphasisAlpha),
    onLeadingIconClick: (() -> Unit)? = null,
    // end icon
    trailingIcon: ImageVector? = null,
    trailingIconContentDescription: String? = null,
    trailingIconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryEmphasisAlpha),
    onTrailingIconClick: (() -> Unit)? = null,
    // sizing / shape
    iconSize: Dp = 24.dp,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    AppTextFieldLayout(
        modifier = modifier,
        title = title,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        shape = shape,
        onFocusChanged = onFocusChanged,
    ) { fieldModifier, interactionSource ->
        val textStyle = LocalTextStyle.current.merge(
            MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledEmphasisAlpha),
            ),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = fieldModifier,
        ) {
            if (leadingIcon != null) {
                AppFieldIcon(
                    icon = leadingIcon,
                    contentDescription = leadingIconContentDescription,
                    tint = leadingIconTint,
                    size = iconSize,
                    enabled = enabled,
                    onClick = onLeadingIconClick,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                if (isSecure) {
                    BasicSecureTextField(
                        state = state,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        textStyle = textStyle,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = imeAction,
                        ),
                        textObfuscationMode = if (isContentVisible) TextObfuscationMode.Visible
                        else TextObfuscationMode.Hidden,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        interactionSource = interactionSource,
                    )
                } else {
                    BasicTextField(
                        state = state,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        textStyle = textStyle,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = imeAction,
                        ),
                        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine
                        else TextFieldLineLimits.Default,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        interactionSource = interactionSource,
                    )
                }
                if (state.text.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryEmphasisAlpha),
                    )
                }
            }
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                AppFieldIcon(
                    icon = trailingIcon,
                    contentDescription = trailingIconContentDescription,
                    tint = trailingIconTint,
                    size = iconSize,
                    enabled = enabled,
                    onClick = onTrailingIconClick,
                )
            }
        }
    }
}

@Composable
private fun AppFieldIcon(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
    size: Dp,
    enabled: Boolean,
    onClick: (() -> Unit)?,
) {
    val clickModifier = if (onClick != null) {
        val clickInteractionSource = remember { MutableInteractionSource() }
        Modifier.clickable(
            interactionSource = clickInteractionSource,
            indication = ripple(bounded = false, radius = size),
            onClick = onClick,
            enabled = enabled,
        )
    } else {
        Modifier
    }
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .size(size)
            .then(clickModifier),
    )
}

@Composable
internal fun AppTextFieldLayout(
    modifier: Modifier = Modifier,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    onFocusChanged: (Boolean) -> Unit = {},
    field: @Composable (fieldModifier: Modifier, interactionSource: MutableInteractionSource) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) { onFocusChanged(isFocused) }

    val background = when {
        isFocused -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        enabled -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
    val fieldModifier = Modifier
        .fillMaxWidth()
        .background(color = background, shape = shape)
        .border(width = 1.dp, color = borderColor, shape = shape)
        .padding(12.dp)

    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryEmphasisAlpha),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        field(fieldModifier, interactionSource)
        if (supportingText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryEmphasisAlpha),
            )
        }
    }
}
