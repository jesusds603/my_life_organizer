package com.example.mylifeorganizer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun AlertDialogWindow(
    title: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String,
    onDismiss: () -> Unit,
    isConfirmButtonEnabled: Boolean = true, // Botón de confirmación habilitado o no
    textFieldValue: String,
    textFieldOnValueChange: (String) -> Unit,
    textFieldLabel: String
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                enabled = isConfirmButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(confirmButtonText, color = themeColors.text1)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(dismissButtonText, color = MaterialTheme.colorScheme.onSecondary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                content = {
                    TextField(
                        value = textFieldValue,
                        onValueChange = { textFieldOnValueChange(it) },
                        label = { Text(textFieldLabel) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = themeColors.text1,
                            unfocusedTextColor = themeColors.text2,
                            focusedContainerColor = themeColors.backGround1,
                            unfocusedContainerColor = themeColors.backGround2,
                        )
                    )
                }
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog,
    )
}
