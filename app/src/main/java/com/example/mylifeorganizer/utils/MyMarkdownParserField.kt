package com.example.mylifeorganizer.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun MyMarkdownParserField(
    modifier: Modifier,
    content: String,
    onChangeContent: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val scrollState = rememberScrollState()

    val isVisualizingNote = appViewModel.isVisualizingNote.value

    fun formatText(text: String): AnnotatedString {
        val annotatedString = buildAnnotatedString {
            text.forEach { char ->
                if (char.isDigit()) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    ) {
                        append(char)
                    }
                    append(" ")
                } else {
                    append(char)
                }
            }
        }
        return annotatedString
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
    ) {

        // Contenido principal
        if (isVisualizingNote) {
            Text(
                text = formatText(content),
                color = themeColors.text1,
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(4.dp),
                fontSize = 12.sp
            )
        } else {
            TextField(
                value = content,
                onValueChange = {
                    onChangeContent(it)
                },
                textStyle = TextStyle(color = themeColors.text1, fontSize = 12.sp),
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text2,
                    focusedContainerColor = themeColors.backGround2,
                    unfocusedContainerColor = themeColors.backGround2,
                    cursorColor = Color.Magenta
                ),
            )
        }
    }
}
