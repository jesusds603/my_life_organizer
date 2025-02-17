package com.example.mylifeorganizer.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mylifeorganizer.R
import com.example.mylifeorganizer.repositories.NotesRepository
import com.example.mylifeorganizer.room.NoteDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HabitNotificationWorker(
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
            Log.d("HabitNotificationWorker", "Worker Habit ejecutado")
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

            // Recoger las tareas del día de forma sincronizada
            val ocurrencesForToday = runBlocking {
                repository.getHabitOccurrencesByDate(currentDate).first()
            }

            Log.d("TaskNotificationWorker", "Tareas encontradas: ${ocurrencesForToday.size} ${currentDate}")

            // Enviar notificaciones solo si hay tareas
            if (ocurrencesForToday.isNotEmpty()) {
                ocurrencesForToday.forEach { ocurrence ->
                    val habit = repository.getHabitById(habitId = ocurrence.habitId)
                    showNotification(habit.title, habit.doItAt)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("HabitNotificationWorker", "Error en el Worker Habit", e)
            Result.failure()
        }
    }

    private fun showNotification(title: String, description: String) {
        Log.d("HabitNotificationWorker", "Intentando mostrar notificación habit: $title")
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "habit_notifications"
        val channelName = "Habit Notifications"
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
        Log.d("HabitNotificationWorker", "Notificación mostrada habit: $title")
    }
}

// -------------------------------------------------------------------------

fun scheduleDailyHabitWorker(context: Context) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0) // Hora de ejecución (12 PM)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    val now = Calendar.getInstance()
    if (calendar.before(now)) {
        // Si la hora ya pasó hoy, programar para mañana
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    val delay = calendar.timeInMillis - now.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<HabitNotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

