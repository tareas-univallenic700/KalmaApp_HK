
package ni.univalle.kalma.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class MoodRepository(private val dao: MoodDao) {
    suspend fun addToday(mood: Int, note: String?) {
        val today = LocalDate.now().toString()
        val existing = dao.getByDate(today)
        if (existing == null) dao.insert(MoodEntry.today(mood, note))
        else dao.insert(existing.copy(mood = mood, note = note))
    }
    fun weekly(from: LocalDate, to: LocalDate): Flow<List<MoodEntry>> =
        dao.streamBetween(from.toString(), to.toString())
    fun all(): Flow<List<MoodEntry>> = dao.streamAll()
}
