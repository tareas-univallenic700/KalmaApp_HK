package ni.univalle.kalma.notifications

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ni.univalle.kalma.KalmaApp

object NotificationPermissions {
    const val POST_NOTIFICATIONS_PERMISSION: String = Manifest.permission.POST_NOTIFICATIONS

    fun requiresRuntimePermission(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun hasRuntimePermission(context: Context): Boolean {
        return !requiresRuntimePermission() ||
            ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }

    fun canPostNotifications(context: Context): Boolean {
        if (!hasRuntimePermission(context)) return false

        val managerCompat = NotificationManagerCompat.from(context)
        val notificationsEnabled = runCatching { managerCompat.areNotificationsEnabled() }.getOrDefault(false)
        if (!notificationsEnabled) return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = manager?.getNotificationChannel(KalmaApp.CHANNEL_WELLBEING)
            if (channel != null && channel.importance == NotificationManager.IMPORTANCE_NONE) {
                return false
            }
        }

        return true
    }
}
