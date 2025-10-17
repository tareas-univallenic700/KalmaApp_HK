
package ni.univalle.kalma.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ni.univalle.kalma.data.AppDatabase
import ni.univalle.kalma.data.MoodRepository
import ni.univalle.kalma.datastore.UserPrefsDataStore
import ni.univalle.kalma.databinding.FragmentProgressBinding
import ni.univalle.kalma.util.DateUtils

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.get(requireContext()).moodDao()
        val repo = MoodRepository(dao)
        val prefs = UserPrefsDataStore(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val daysIso = DateUtils.recentDaysIso(7)
            val data = repo.weekly(daysIso.first(), daysIso.last()).first()

            val map = data.associateBy({ it.dateIso }, { it.mood.toFloat() })
            val entries = daysIso.map { iso -> map[iso] ?: Float.NaN }

            val labels = daysIso.map { DateUtils.weekdayLabel(it) }

            binding.chart.setData(entries, labels, 0f, 4f)

            val count = data.size
            val avg = if (count > 0) data.map { it.mood }.average() else 0.0
            binding.tvSummary.text = "Registros: %d\nÁnimo promedio: %.2f".format(count, avg)

            val streak = prefs.streakFlow.first()
            binding.tvStreak.text = "Racha: %d días".format(streak)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
