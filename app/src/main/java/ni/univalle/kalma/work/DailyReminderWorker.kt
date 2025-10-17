
package ni.univalle.kalma.work

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        NotificationManagerCompat.from(applicationContext).notify(Random.nextInt(), notification)
        return Result.success()
    }
}
