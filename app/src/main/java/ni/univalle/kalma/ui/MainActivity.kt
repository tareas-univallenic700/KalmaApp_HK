
package ni.univalle.kalma.ui

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import ni.univalle.kalma.R
import ni.univalle.kalma.databinding.ActivityMainBinding
import ni.univalle.kalma.notifications.NotificationPermissions
import ni.univalle.kalma.work.Scheduler

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && NotificationPermissions.canPostNotifications(this)) {
            scheduleDailyWorker()
        } else {
            cancelDailyWorker()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navHost?.let { binding.bottomNav.setupWithNavController(it.navController) }
    }

    override fun onStart() {
        super.onStart()
        ensureNotificationPermissionThenSchedule()
    }

    private fun ensureNotificationPermissionThenSchedule() {
        if (NotificationPermissions.canPostNotifications(this)) {
            scheduleDailyWorker()
            return
        }

        cancelDailyWorker()

        if (NotificationPermissions.requiresRuntimePermission() &&
            !NotificationPermissions.hasRuntimePermission(this)
        ) {
            requestPermission.launch(NotificationPermissions.POST_NOTIFICATIONS_PERMISSION)
        }
    }

    private fun scheduleDailyWorker() {
        Scheduler.scheduleDailyReminder(WorkManager.getInstance(this), 20, 0)
    }

    private fun cancelDailyWorker() {
        Scheduler.cancelDailyReminder(WorkManager.getInstance(this))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
