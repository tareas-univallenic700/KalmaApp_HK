
package ni.univalle.kalma.ui.breath

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ni.univalle.kalma.databinding.FragmentBreathingBinding

class BreathingFragment : Fragment() {
    private var _binding: FragmentBreathingBinding? = null
    private val binding get() = _binding!!
    private var timer: CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBreathingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnStart.setOnClickListener {
            timer?.cancel()
            timer = object : CountDownTimer(60_000, 1_000) {
                override fun onTick(ms: Long) { binding.tvTimer.text = (ms/1000).toString() }
                override fun onFinish() { binding.tvTimer.text = "0" }
            }.start()
        }
    }

    override fun onDestroyView() {
        timer?.cancel()
        _binding = null
        super.onDestroyView()
    }
}
