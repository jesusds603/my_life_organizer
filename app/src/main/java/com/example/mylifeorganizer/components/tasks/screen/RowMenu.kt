package com.example.mylifeorganizer.components.tasks.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.AppViewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowMenu(
    selectedDueOrCompleted: String,
    changeSelectedDueOrCompleted: (String) -> Unit,
) {
    val appViewModel: AppViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val themeColors = themeViewModel.themeColors.value
    val isLangEng = appViewModel.isLangEng.value


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = themeColors.backGround1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f)
                .background(color = if (selectedDueOrCompleted == "due") themeColors.bgCardNote else themeColors.backGround1),
            onClick = {
                changeSelectedDueOrCompleted("due")
            }
        ) {
            Text(
                text = if(isLangEng) "Pending" else "Pendientes",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            modifier = Modifier
                .weight(1f)
                .background(color = if (selectedDueOrCompleted == "completed") themeColors.bgCardNote else themeColors.backGround1),
            onClick = {
                changeSelectedDueOrCompleted("completed")
            }
        ) {
            Text(
                text = if(isLangEng) "Completed" else "Completadas",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            modifier = Modifier
                .weight(1f)
                .background(color = if (selectedDueOrCompleted == "notDone") themeColors.bgCardNote else themeColors.backGround1),
            onClick = {
                changeSelectedDueOrCompleted("notDone")
            }
        ) {
            Text(
                text = if(isLangEng) "Not Done" else "No completadas",
                color = themeColors.text1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}