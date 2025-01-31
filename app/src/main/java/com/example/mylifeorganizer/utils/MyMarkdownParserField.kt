package com.example.mylifeorganizer.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.utils.parsers.formatText
import com.example.mylifeorganizer.utils.parsers.processLists
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyMarkdownParserField(
    modifier: Modifier,
    content: String,
    onChangeContent: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val scrollStateEdit = rememberScrollState()
    val scrollStateView = rememberScrollState()

    val isVisualizingNote = appViewModel.isVisualizingNote.value

    // Detectar el estado del foco y actualizar la posición del scroll
    val keyboardController = LocalSoftwareKeyboardController.current


    // Mantener la posición del cursor
    val textFieldFocusState = remember { mutableStateOf(TextFieldValue(content)) }


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
                    .verticalScroll(scrollStateView)
                    .padding(4.dp),
                fontSize = 14.sp
            )
        } else {
            // Detectar si el TextField tiene foco para actualizar el scroll
            val isFocused = remember { mutableStateOf(false) }

            TextField(
                value = content,
                onValueChange = {
                    onChangeContent(it)
                },
                textStyle = TextStyle(color = themeColors.text1, fontSize = 14.sp),
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollStateEdit)
                    .onFocusChanged { focusState ->
                        // Actualizamos el estado del foco
                        isFocused.value = focusState.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = themeColors.text1,
                    unfocusedTextColor = themeColors.text2,
                    focusedContainerColor = themeColors.backGround2,
                    unfocusedContainerColor = themeColors.backGround2,
                    cursorColor = Color.Magenta
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Esconde el teclado cuando se presiona 'Done'
                    }
                )
            )

            // Usar LaunchedEffect solo cuando el TextField tenga el foco
            LaunchedEffect(isFocused.value) {
                if (isFocused.value) {
                    // Aseguramos que el scroll se mueva para mostrar la posición del cursor
                    scrollStateEdit.animateScrollTo(scrollStateEdit.value)
                }
            }
        }
    }
}


