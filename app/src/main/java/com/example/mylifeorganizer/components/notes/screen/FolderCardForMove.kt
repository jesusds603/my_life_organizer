package com.example.mylifeorganizer.components.notes.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.viewmodel.ThemeViewModel


@Composable
fun FolderCardForMove(
    folderId: Long,
    folderName: String,
    isSelected: Boolean,
    isExpanded: Boolean,
    onFolderClicked: (Long) -> Unit,
    onExpandClicked: (Long) -> Unit
) {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value

    Row(
        modifier = Modifier
            .background(
                color = if (isSelected) {
                    themeColors.backGround1
                } else {
                    themeColors.bgCardFolder
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .clickable {
                onFolderClicked(folderId)
                onExpandClicked(folderId)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_folder_24),
                contentDescription = null,
                tint = themeColors.text1,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(1.dp))
            Icon(
                painter = if (isExpanded) {
                    painterResource(id = R.drawable.baseline_keyboard_arrow_down_24)
                } else {
                    painterResource(id = R.drawable.baseline_keyboard_arrow_right_24)
                },
                contentDescription = null,
                tint = themeColors.text1,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = folderName,
                color = themeColors.text1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}