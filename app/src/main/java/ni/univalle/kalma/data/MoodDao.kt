
package ni.univalle.kalma.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY dateIso DESC")
    fun streamAll(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE dateIso BETWEEN :from AND :to ORDER BY dateIso ASC")
    fun streamBetween(from: String, to: String): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE dateIso = :dateIso LIMIT 1")
    suspend fun getByDate(dateIso: String): MoodEntry?
}
