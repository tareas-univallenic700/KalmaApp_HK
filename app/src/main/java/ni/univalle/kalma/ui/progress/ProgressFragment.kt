
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            val to = LocalDate.now()
            val from = to.minusDays(6)
            val data = repo.weekly(from, to).first()

            val days = (0..6).map { from.plusDays(it.toLong()) }
            val map = data.associateBy({ LocalDate.parse(it.dateIso) }, { it.mood.toFloat() })
            val entries = days.map { d -> map[d] ?: Float.NaN }

            val fmt = DateTimeFormatter.ofPattern("E")
            val labels = days.map { it.format(fmt) }

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
