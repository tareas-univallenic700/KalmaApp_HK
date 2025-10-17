
package ni.univalle.kalma.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ni.univalle.kalma.data.AppDatabase
import ni.univalle.kalma.data.MoodRepository
import ni.univalle.kalma.datastore.UserPrefsDataStore
import ni.univalle.kalma.databinding.FragmentMoodBinding
import java.time.LocalDate

class MoodFragment : Fragment() {

    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!
    private var selectedMood: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = AppDatabase.get(requireContext()).moodDao()
        val repo = MoodRepository(dao)
        val prefs = UserPrefsDataStore(requireContext())

        val moodViews = listOf<TextView>(binding.mood0, binding.mood1, binding.mood2, binding.mood3, binding.mood4)
        moodViews.forEachIndexed { idx, tv -> tv.setOnClickListener { selectedMood = idx } }

        binding.btnSave.setOnClickListener {
            val note = binding.etNote.text?.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                repo.addToday(selectedMood, note)
                val today = LocalDate.now()
                val last = prefs.lastMoodDateFlow.first()
                val prevStreak = prefs.streakFlow.first()
                val newStreak = when (last) {
                    today.toString() -> prevStreak
                    today.minusDays(1).toString() -> prevStreak + 1
                    else -> 1
                }
                prefs.setLastMood(today.toString(), newStreak)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
