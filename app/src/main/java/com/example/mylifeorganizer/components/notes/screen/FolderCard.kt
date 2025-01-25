package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.room.FolderEntity
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.NoteViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun FolderCard(
    noteViewModel: NoteViewModel,
    expandedFolders: Set<Long>,
    onFolderClick: (Set<Long>) -> Unit,
    folder: FolderEntity,
    depth: Int
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = (16 * depth).dp, vertical = 8.dp)
            .background(themeColors.backGround4, shape = RoundedCornerShape(8.dp))
            .clickable {
                onFolderClick(
                    if (expandedFolders.contains(folder.folderId)) {
                        expandedFolders - folder.folderId
                    } else {
                        expandedFolders + folder.folderId
                    }
                )
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f) // Esto asegura que el texto y el icono ocupen todo el espacio disponible
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_folder_24),
                contentDescription = null,
                tint = themeColors.text1,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre el icono y el texto
            Text(
                text = folder.name,
                color = themeColors.text1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // Manejo de texto largo
            )
        }

        Box {
            Icon(
                painter = painterResource(R.drawable.baseline_more_vert_24),
                contentDescription = null,
                tint = themeColors.text1,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        showDialog = true
                    }
            )

            if (showDialog) {
                DropdownMenu(
                    expanded = showDialog,
                    onDismissRequest = { showDialog = false },
                    modifier = Modifier.background(themeColors.backGround1)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Add Note",
                                color = themeColors.text1
                            )
                        },
                        onClick = {
                            appViewModel.changeIdFolderForAddingNote(folder.folderId)
                            appViewModel.toggleAddingNote()
                            showDialog = false
                        },
                        modifier = Modifier.background(themeColors.backGround3)
                    )
                }
            }
        }
    }
}
