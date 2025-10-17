
package ni.univalle.kalma.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import ni.univalle.kalma.R
import ni.univalle.kalma.databinding.ActivityMainBinding
import ni.univalle.kalma.work.Scheduler

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
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
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                cancelDailyWorker()
                requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                scheduleDailyWorker()
            }
        } else scheduleDailyWorker()
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
