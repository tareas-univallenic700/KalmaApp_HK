
package ni.univalle.kalma.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateIso: String,
    val mood: Int,
    val note: String?
) {
    companion object {
        fun today(mood: Int, note: String?) = MoodEntry(
            id = 0,
            dateIso = LocalDate.now().toString(),
            mood = mood,
            note = note
        )
    }
}
