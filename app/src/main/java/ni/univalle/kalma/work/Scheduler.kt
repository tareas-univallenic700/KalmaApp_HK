
package ni.univalle.kalma.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object Scheduler {
    private const val WORK_NAME = "daily_reminder"

    fun scheduleDailyReminder(wm: WorkManager, hour: Int = 20, minute: Int = 0) {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        var nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!nextRun.isAfter(now)) {
            nextRun = nextRun.plusDays(1)
        }
        val initialDelay = Duration.between(now, nextRun).toMinutes()
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .build()
        wm.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun cancelDailyReminder(wm: WorkManager) {
        wm.cancelUniqueWork(WORK_NAME)
    }
}
