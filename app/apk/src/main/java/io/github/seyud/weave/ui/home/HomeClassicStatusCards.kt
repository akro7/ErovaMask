package io.github.seyud.weave.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.seyud.weave.core.R as CoreR
import io.github.seyud.weave.ui.theme.LocalIsMonetTheme
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.ChevronForward
import top.yukonga.miuix.kmp.icon.extended.Download
import top.yukonga.miuix.kmp.icon.extended.Update
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.TiltFeedback
import top.yukonga.miuix.kmp.utils.pressable

@Composable
internal fun ClassicMagiskCard(
    magiskState: HomeViewModel.State,
    installedVersion: String,
    expanded: Boolean,
    onCardClick: () -> Unit,
    onInstallClick: () -> Unit,
) {
    val context = LocalContext.current
    val isMonetTheme = LocalIsMonetTheme.current
    val isInteractive = magiskState != HomeViewModel.State.LOADING
    val accentColor = if (isMonetTheme) {
        MiuixTheme.colorScheme.primary
    } else {
        colorResource(id = CoreR.color.weave_brand_main)
    }
    val actionText = if (magiskState == HomeViewModel.State.OUTDATED) {
        context.getString(CoreR.string.update)
    } else {
        context.getString(CoreR.string.install)
    }
    val actionIcon = if (magiskState == HomeViewModel.State.OUTDATED) {
        MiuixIcons.Update
    } else {
        MiuixIcons.Download
    }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "classicMagiskCardChevronRotation"
    )

    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .then(
                if (isInteractive) {
                    Modifier
                } else {
                    Modifier.pressable(
                        interactionSource = null,
                        indication = TiltFeedback(),
                        delay = null
                    )
                }
            ),
        colors = CardDefaults.defaultColors(),
        pressFeedbackType = if (isInteractive) PressFeedbackType.Tilt else PressFeedbackType.None,
        onClick = if (isInteractive) onCardClick else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClassicWeaveCardIcon(
                isMonetTheme = isMonetTheme,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Weave",
                    style = MiuixTheme.textStyles.title3,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = installedVersion,
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    fontWeight = FontWeight.Medium
                )
            }

            when (magiskState) {
                HomeViewModel.State.LOADING -> {
                    CircularProgressIndicator(
                        size = 24.dp,
                        strokeWidth = 2.dp
                    )
                }

                else -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        InlineCardActionButton(
                            icon = actionIcon,
                            text = actionText,
                            accentColor = accentColor,
                            onPressed = onInstallClick
                        )
                        if (isInteractive) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = MiuixIcons.ChevronForward,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer {
                                        rotationZ = chevronRotation
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassicWeaveCardIcon(
    isMonetTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isMonetTheme) {
        Image(
            painter = painterResource(id = CoreR.drawable.ic_weave_card),
            contentDescription = null,
            modifier = modifier
        )
        return
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = CoreR.drawable.ic_weave_card),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.primary),
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = CoreR.drawable.ic_weave_card_monet_detail),
            contentDescription = null,
            colorFilter = ColorFilter.tint(lerp(MiuixTheme.colorScheme.primary, Color.White, 0.28f)),
            modifier = Modifier.fillMaxSize()
        )
    }
}
