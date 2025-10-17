package ni.univalle.kalma.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private val isoFormat = ThreadLocal.withInitial {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    private val weekdayFormat = ThreadLocal.withInitial {
        SimpleDateFormat("E", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    fun todayIso(): String = isoFormat.get().format(Date())

    fun yesterdayIso(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return isoFormat.get().format(cal.time)
    }

    fun addDaysIso(startIso: String, days: Int): String {
        val cal = calendarFromIso(startIso)
        cal.add(Calendar.DATE, days)
        return isoFormat.get().format(cal.time)
    }

    fun recentDaysIso(count: Int): List<String> {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val result = ArrayList<String>(count)
        val cal = today.clone() as Calendar
        cal.add(Calendar.DATE, -(count - 1))
        repeat(count) {
            result.add(isoFormat.get().format(cal.time))
            cal.add(Calendar.DATE, 1)
        }
        return result
    }

    fun weekdayLabel(iso: String): String {
        val cal = calendarFromIso(iso)
        return weekdayFormat.get().format(cal.time)
    }

    private fun calendarFromIso(iso: String): Calendar {
        val date = isoFormat.get().parse(iso)
        val cal = Calendar.getInstance()
        if (date != null) cal.time = date
        return cal
    }
}
