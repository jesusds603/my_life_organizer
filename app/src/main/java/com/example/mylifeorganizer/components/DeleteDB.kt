package com.example.mylifeorganizer.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylifeorganizer.viewmodel.ThemeViewModel

@Composable
fun DeleteDB() {
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = viewModel()

    var dbName by remember { mutableStateOf("") }
    var themeColors = themeViewModel.themeColors.value

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Eliminar base de datos",
            color = themeColors.text1
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input para el nombre de la base de datos
        OutlinedTextField(
            value = dbName,
            onValueChange = { dbName = it },
            label = { Text( text = "Nombre de la base de datos" )},
            placeholder = { Text( text = "Por ejemplo, database.db ", color = themeColors.text3)},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Eliminar base de datos",
                color = themeColors.text1
            )
        }
    }
}

fun deleteDatabase(context: Context, dbName: String) {
    val deleted = context.deleteDatabase(dbName)

    if (deleted) {
        Log.d("Database", "Base de datos $dbName eliminada correctamente")
    } else {
        Log.d("Database", "No se pudo eliminar la base de datos $dbName o no existe")
    }
}