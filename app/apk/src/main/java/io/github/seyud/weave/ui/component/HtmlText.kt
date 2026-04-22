package io.github.seyud.weave.ui.component

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import top.yukonga.miuix.kmp.theme.MiuixTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HtmlText(
    content: String,
    baseUrl: String? = null,
    modifier: Modifier = Modifier,
    onLoadingChange: (Boolean) -> Unit = {},
) {
    val textColor = "#%06X".format(0xFFFFFF and MiuixTheme.colorScheme.onBackground.toArgb())
    val linkColor = "#%06X".format(0xFFFFFF and MiuixTheme.colorScheme.primary.toArgb())
    val html = """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    background: transparent;
                }
                body {
                    color: $textColor;
                    font-size: 14px;
                    line-height: 1.6;
                    word-break: break-word;
                }
                a { color: $linkColor; }
                img { max-width: 100%; height: auto; }
                pre, code {
                    white-space: pre-wrap;
                    word-break: break-word;
                    background: transparent;
                }
                pre { overflow: auto; }
                blockquote {
                    margin-left: 0;
                    padding-left: 12px;
                    border-left: 3px solid rgba(127,127,127,.35);
                }
            </style>
        </head>
        <body>$content</body>
        </html>
    """.trimIndent()
    val loadKey = "${baseUrl.orEmpty()}\u0000$html"

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clipToBounds(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                settings.domStorageEnabled = true
                settings.offscreenPreRaster = true
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                overScrollMode = WebView.OVER_SCROLL_NEVER
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                webViewClient = object : WebViewClient() {}
            }
        },
        update = { webView ->
            if (webView.tag != loadKey) {
                webView.tag = loadKey
                onLoadingChange(true)
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageCommitVisible(view: WebView?, url: String?) {
                        onLoadingChange(false)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onLoadingChange(false)
                    }
                }
                webView.loadDataWithBaseURL(baseUrl, html, "text/html", "utf-8", null)
            }
        },
        onRelease = { webView ->
            onLoadingChange(false)
            webView.destroy()
        },
    )
}
