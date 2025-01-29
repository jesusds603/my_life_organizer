package com.example.mylifeorganizer.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.runtime.Composable
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
        return try {
            Log.d("TaskNotificationWorker", "Worker ejecutado")
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

            // Recoger las tareas del día de forma sincronizada
            val ocurrencesForToday = runBlocking {
                repository.getOccurrencesByDate(currentDate).first()
            }

            Log.d("TaskNotificationWorker", "Tareas encontradas: ${ocurrencesForToday.size} ${currentDate}")

            // Enviar notificaciones solo si hay tareas
            if (ocurrencesForToday.isNotEmpty()) {
                ocurrencesForToday.forEach { ocurrence ->
                    val task = repository.getTaskById(ocurrence.taskId)
                    showNotification(task.task.title, task.task.description)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("TaskNotificationWorker", "Error en el Worker", e)
            Result.failure()
        }
    }

    private fun showNotification(title: String, description: String) {
        Log.d("TaskNotificationWorker", "Intentando mostrar notificación: $title")
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
            .setColor(Color.Magenta.toArgb())
            .build()

        // Mostrar la notificación con un ID único
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d("TaskNotificationWorker", "Notificación mostrada: $title")
    }
}

// -------------------------------------------------------------------------

fun scheduleDailyTaskWorker(context: Context) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 12) // Hora de ejecución (12 PM)
        set(Calendar.MINUTE, 38)
        set(Calendar.SECOND, 30)
    }

    val now = Calendar.getInstance()
    if (calendar.before(now)) {
        // Si la hora ya pasó hoy, programar para mañana
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    val delay = calendar.timeInMillis - now.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<TaskNotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}