
package ni.univalle.kalma.data

import kotlinx.coroutines.flow.Flow
import ni.univalle.kalma.util.DateUtils

class MoodRepository(private val dao: MoodDao) {
    suspend fun addToday(mood: Int, note: String?) {
        val today = DateUtils.todayIso()
        val existing = dao.getByDate(today)
        if (existing == null) dao.insert(MoodEntry.today(mood, note))
        else dao.insert(existing.copy(mood = mood, note = note))
    }
    fun weekly(fromIso: String, toIso: String): Flow<List<MoodEntry>> =
        dao.streamBetween(fromIso, toIso)
    fun all(): Flow<List<MoodEntry>> = dao.streamAll()
}
