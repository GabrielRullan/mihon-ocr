package eu.kanade.tachiyomi.data.anki

import android.content.Context
import androidx.core.content.ContextCompat
import com.ichi2.anki.api.AddContentApi
import tachiyomi.domain.dictionary.model.DictionaryEntry

class AnkiManager(private val context: Context) {

    private val api = AddContentApi(context)

    fun isAnkiInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.ichi2.anki", 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, "com.ichi2.anki.permission.READ_WRITE_DATABASE") ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun addCard(entry: DictionaryEntry, contextSentence: String? = null): Boolean {
        if (!hasPermission()) return false

        val deckId = getOrCreateDeck("Mihon OCR") ?: return false
        val modelId = getOrCreateModel() ?: return false

        val fields = arrayOf(
            entry.simplified, // Word
            entry.pinyin, // Pinyin
            entry.english, // Definition
            contextSentence ?: "", // Context
            entry.traditional, // Traditional
        )

        val noteId = api.addNote(modelId, deckId, fields, setOf("mihon-ocr"))
        return noteId != null
    }

    private fun getOrCreateDeck(deckName: String): Long? {
        val decks = api.deckList
        if (decks != null) {
            for (deckEntry in decks.entries) {
                if (deckEntry.value == deckName) return deckEntry.key
            }
        }
        return api.addNewDeck(deckName)
    }

    private fun getOrCreateModel(): Long? {
        val models = api.modelList
        if (models != null) {
            for (modelEntry in models.entries) {
                if (modelEntry.value == "Mihon Chinese") return modelEntry.key
            }
        }

        // Create a new model if it doesn't exist
        return api.addNewCustomModel(
            "Mihon Chinese",
            arrayOf("Word", "Pinyin", "Definition", "Context", "Traditional"),
            arrayOf("Card 1"),
            arrayOf("{{Word}}"),
            arrayOf("{{Pinyin}}<br><br>{{Definition}}<br><br><i>{{Context}}</i><br><small>{{Traditional}}</small>"),
            ".card { font-family: sans-serif; text-align: center; font-size: 20px; }",
            null,
            0,
        )
    }
}
