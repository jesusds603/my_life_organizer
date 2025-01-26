package com.example.mylifeorganizer.utils

import android.annotation.SuppressLint
import android.webkit.WebView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.rememberWebViewState
import io.noties.markwon.Markwon

@Composable
fun TextMarkdown(markdown: String) {
    // Obtener el ViewModel de tema
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    // Renderizar el Markdown con los colores del tema
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                textSize = 16f
                setPadding(16, 16, 16, 16)
                setTextColor(themeColors.text1.toArgb())

                // Renderizar el contenido Markdown
                val markwon = Markwon.create(context)
                markwon.setMarkdown(this, markdown)
            }
        },
        update = { textView ->
            // Actualizar el contenido Markdown
            val markwon = Markwon.create(textView.context)
            markwon.setMarkdown(textView, markdown)
        }
    )
}


//@SuppressLint("SetJavaScriptEnabled")
//@Composable
//fun LaTeXView(latex: String) {
//
//    var webView: WebView? by remember { mutableStateOf(null) }
//
//    val state = rememberWebViewState("file:///android_asset/latex_render.html")
//
//    if (state.loadingState is LoadingState.Finished) {
//        webView?.loadUrl("javascript:addBody('${latex}')")
//    }
//    com.google.accompanist.web.WebView(
//        state = state,
//        modifier = Modifier,
//        onCreated = {
//            it.settings.javaScriptEnabled = true
//            webView = it
//            it.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
//            it.setBackgroundColor(0)
//        }
//    )
//}