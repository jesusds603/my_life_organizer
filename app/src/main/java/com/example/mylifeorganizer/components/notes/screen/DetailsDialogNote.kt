package com.example.mylifeorganizer.components.notes.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.room.NoteWithCategories
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsDialogNote(
    changeShowDetailsDialog: (Boolean) -> Unit,
    noteWithCategories: NoteWithCategories?
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value

    AlertDialog(
        onDismissRequest = {
            changeShowDetailsDialog(false)
        },
        confirmButton = {
            Button(
                onClick = {
                    changeShowDetailsDialog(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColors.backGround1
                )
            ) {
                Text(
                    text = "OK"
                )
            }
        },
        text = {
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "${if(isLangEng) "Title" else "Título"}: ${noteWithCategories?.note?.title}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "${noteWithCategories?.note?.content?.length} ${if(isLangEng) "characters" else "carácteres"}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "${noteWithCategories?.note?.content?.split("\\s+".toRegex())?.filter { it.isNotBlank() }?.size ?: 0} ${if(isLangEng) "words" else "palabras"}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "${if(isLangEng) "Categories" else "Categorías"}: ${noteWithCategories?.categories?.joinToString(", ") { it.name }}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "${if(isLangEng) "Created at" else "Creado el"}: ${formatDate(noteWithCategories?.note?.createdAt ?: 0)}",
                    color = themeColors.text1
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "${if(isLangEng) "Updated at" else "Actualizado el"}: ${formatDate(noteWithCategories?.note?.updatedAt ?: 0)}",
                    color = themeColors.text1
                )
            }
        },
        title = {
            Text(
                text = if(isLangEng) "Details" else "Detalles",
                style = MaterialTheme.typography.titleLarge,
                color = themeColors.text1
            )
        },
        containerColor = themeColors.bgDialog
    )
}