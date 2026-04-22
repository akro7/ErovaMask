package io.github.seyud.weave.ui.component

import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.text.method.LinkMovementMethod
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import io.github.seyud.weave.core.di.ServiceLocator
import top.yukonga.miuix.kmp.theme.MiuixTheme

private const val TEXTVIEW_TAG = "markdownTextView"

/**
 * Compose Markdown 渲染组件
 * 使用 AndroidView 包裹 Markwon 的 TextView 实现 Markdown 渲染
 * 参考 KernelSU 的实现方式
 */
@Composable
fun MarkdownText(
    content: String,
    modifier: Modifier = Modifier
) {
    val contentColor = MiuixTheme.colorScheme.onBackground.toArgb()

    AndroidView(
        factory = { context ->
            val frameLayout = FrameLayout(context)
            val scrollView = ScrollView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val textView = TextView(context).apply {
                tag = TEXTVIEW_TAG
                movementMethod = LinkMovementMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    breakStrategy = LineBreaker.BREAK_STRATEGY_SIMPLE
                }
                hyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            scrollView.addView(textView)
            frameLayout.addView(scrollView)
            frameLayout
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clipToBounds(),
        update = { frameLayout ->
            frameLayout.findViewWithTag<TextView>(TEXTVIEW_TAG)?.let { textView ->
                ServiceLocator.markwon.setMarkdown(textView, content)
                textView.setTextColor(contentColor)
            }
        }
    )
}
