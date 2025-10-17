
package ni.univalle.kalma.work

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ni.univalle.kalma.KalmaApp
import ni.univalle.kalma.R
import kotlin.random.Random

class DailyReminderWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    private val messages = listOf(
        "¿Cómo te sientes hoy? Tómate 20 segundos para notarlo.",
        "Pequeño check-in: respira profundo y regístralo en Kalma.",
        "Tu bienestar cuenta. ¿Anotamos tu estado de ánimo?"
    )
    override suspend fun doWork(): Result {
        val message = messages[Random.nextInt(messages.size)]
        val notification = NotificationCompat.Builder(applicationContext, KalmaApp.CHANNEL_WELLBEING)
            .setSmallIcon(R.drawable.kalma_mascota)
            .setContentTitle("Kalma")
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val hasRuntimePermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        val canNotify = notificationManager.areNotificationsEnabled() && hasRuntimePermission

        if (canNotify) {
            notificationManager.notify(Random.nextInt(), notification)
        }
        return Result.success()
    }
}
