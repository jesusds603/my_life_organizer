package com.example.mylifeorganizer.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.runtime.Composable
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.NoteDB
import com.example.mylifeorganizer.viewmodel.AppViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TaskNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val repository: NotesRepository


    init {
        val database = NoteDB.getInstance(context)
        repository = NotesRepository(database)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        // Recoger las tareas del día de forma sincronizada
        val tasksForToday = runBlocking {
            repository.getTasksByDueDate(currentDate).first()
        }

        // Enviar notificaciones solo si hay tareas
        if (tasksForToday.isNotEmpty()) {
            tasksForToday.forEach { task ->
                showNotification(task.title, task.description)
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, description: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "task_notifications"
        val channelName = "Task Notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        // Crear canal de notificación si no existe (solo necesario para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Mostrar la notificación con un ID único
        notificationManager.notify(title.hashCode(), notification)
    }
}
