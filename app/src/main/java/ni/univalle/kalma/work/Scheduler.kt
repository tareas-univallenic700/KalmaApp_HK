
package ni.univalle.kalma.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object Scheduler {
    private const val WORK_NAME = "daily_reminder"

    fun scheduleDailyReminder(wm: WorkManager, hour: Int = 20, minute: Int = 0) {
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS).build()
        wm.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun cancelDailyReminder(wm: WorkManager) {
        wm.cancelUniqueWork(WORK_NAME)
    }
}
