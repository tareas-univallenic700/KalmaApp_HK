
package ni.univalle.kalma

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class KalmaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_WELLBEING, "Bienestar diario", NotificationManager.IMPORTANCE_DEFAULT)
            ch.description = "Recordatorios para registrar tu estado de ánimo"
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }
    companion object { const val CHANNEL_WELLBEING = "kalma_wellbeing" }
}
