
package ni.univalle.kalma.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "kalma_prefs")

object Keys {
    val onboardingDone = booleanPreferencesKey("onboarding_done")
    val lastMoodDate = stringPreferencesKey("last_mood_date")
    val streakDays = intPreferencesKey("streak_days")
}

class UserPrefsDataStore(private val context: Context) {
    val onboardingDoneFlow: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.onboardingDone] ?: false }
    val streakFlow: Flow<Int> =
        context.dataStore.data.map { it[Keys.streakDays] ?: 0 }
    val lastMoodDateFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.lastMoodDate] }

    suspend fun setLastMood(dateIso: String, streak: Int) {
        context.dataStore.edit {
            it[Keys.lastMoodDate] = dateIso
            it[Keys.streakDays] = streak
        }
    }
}
