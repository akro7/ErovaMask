package io.github.seyud.weave.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.seyud.weave.core.R as CoreR
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.ChevronForward
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import top.yukonga.miuix.kmp.utils.TiltFeedback
import top.yukonga.miuix.kmp.utils.pressable

@Composable
internal fun SupportCard(
    onLinkPressed: (String) -> Unit,
    expanded: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "supportCardChevronRotation"
    )

    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .then(
                if (onExpandedChange == null) {
                    Modifier.pressable(
                        interactionSource = null,
                        indication = TiltFeedback(),
                        delay = null
                    )
                } else {
                    Modifier
                }
            ),
        pressFeedbackType = if (onExpandedChange != null) PressFeedbackType.Tilt else PressFeedbackType.None,
        onClick = onExpandedChange?.let { { it(!expanded) } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(CoreR.string.home_support_title),
                    fontSize = MiuixTheme.textStyles.headline1.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (onExpandedChange != null) {
                    Icon(
                        imageVector = MiuixIcons.ChevronForward,
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer {
                                rotationZ = chevronRotation
                            }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(durationMillis = 220)) +
                    expandVertically(animationSpec = tween(durationMillis = 260)),
                exit = fadeOut(animationSpec = tween(durationMillis = 180)) +
                    shrinkVertically(animationSpec = tween(durationMillis = 220))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = context.getString(CoreR.string.home_support_content),
                        fontSize = MiuixTheme.textStyles.body2.fontSize,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SupportIcon(
                            iconRes = CoreR.drawable.ic_patreon,
                            contentDescription = context.getString(CoreR.string.patreon),
                            onClick = { onLinkPressed(IconLink.Patreon.link) }
                        )
                        SupportIcon(
                            iconRes = CoreR.drawable.ic_paypal,
                            contentDescription = context.getString(CoreR.string.paypal),
                            onClick = { onLinkPressed(IconLink.PayPal.Project.link) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportIcon(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
internal fun FollowCard(
    onLinkPressed: (String) -> Unit,
    expanded: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "followCardChevronRotation"
    )

    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .then(
                if (onExpandedChange == null) {
                    Modifier.pressable(
                        interactionSource = null,
                        indication = TiltFeedback(),
                        delay = null
                    )
                } else {
                    Modifier
                }
            ),
        pressFeedbackType = if (onExpandedChange != null) PressFeedbackType.Tilt else PressFeedbackType.None,
        onClick = onExpandedChange?.let { { it(!expanded) } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(CoreR.string.home_follow_title),
                    fontSize = MiuixTheme.textStyles.headline1.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (onExpandedChange != null) {
                    Icon(
                        imageVector = MiuixIcons.ChevronForward,
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer {
                                rotationZ = chevronRotation
                            }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(durationMillis = 220)) +
                    expandVertically(animationSpec = tween(durationMillis = 260)),
                exit = fadeOut(animationSpec = tween(durationMillis = 180)) +
                    shrinkVertically(animationSpec = tween(durationMillis = 220))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))

                    DeveloperLinksRow(
                        handle = DeveloperItem.John.handle,
                        links = DeveloperItem.John.items,
                        onLinkPressed = onLinkPressed
                    )
                    DeveloperLinksRow(
                        handle = DeveloperItem.Vvb.handle,
                        links = DeveloperItem.Vvb.items,
                        onLinkPressed = onLinkPressed
                    )
                    DeveloperLinksRow(
                        handle = DeveloperItem.YU.handle,
                        links = DeveloperItem.YU.items,
                        onLinkPressed = onLinkPressed
                    )
                    DeveloperLinksRow(
                        handle = DeveloperItem.Seyud.handle,
                        links = DeveloperItem.Seyud.items,
                        onLinkPressed = onLinkPressed
                    )
                    DeveloperLinksRow(
                        handle = DeveloperItem.Rikka.handle,
                        links = DeveloperItem.Rikka.items,
                        onLinkPressed = onLinkPressed
                    )
                    DeveloperLinksRow(
                        handle = DeveloperItem.Canyie.handle,
                        links = DeveloperItem.Canyie.items,
                        onLinkPressed = onLinkPressed
                    )
                }
            }
        }
    }
}

@Composable
private fun DeveloperLinksRow(
    handle: String,
    links: List<IconLink>,
    onLinkPressed: (String) -> Unit,
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = handle,
            fontSize = MiuixTheme.textStyles.body2.fontSize,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp)
        )

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            links.forEach { link ->
                val iconRes = when (link) {
                    is IconLink.Patreon -> CoreR.drawable.ic_patreon
                    is IconLink.PayPal -> CoreR.drawable.ic_paypal
                    is IconLink.Twitter -> CoreR.drawable.ic_twitter
                    is IconLink.Github -> CoreR.drawable.ic_github
                    is IconLink.Sponsor -> CoreR.drawable.ic_favorite
                }

                IconButton(
                    onClick = { onLinkPressed(link.link) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = context.getString(link.title),
                        colorFilter = ColorFilter.tint(MiuixTheme.colorScheme.onSurface),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
