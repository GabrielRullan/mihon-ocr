package tachiyomi.data.dictionary

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import tachiyomi.data.Database
import tachiyomi.domain.dictionary.model.DictionaryEntry
import tachiyomi.domain.dictionary.repository.DictionaryRepository

class DictionaryRepositoryImpl(
    private val database: Database,
) : DictionaryRepository {

    override suspend fun getEntryBySimplified(simplified: String): DictionaryEntry? {
        return database.dictionaryQueries
            .getEntryBySimplified(simplified, ::mapDictionary)
            .awaitAsOneOrNull()
    }

    override suspend fun getEntryBySimplifiedOrTraditional(text: String): DictionaryEntry? {
        return database.dictionaryQueries
            .getEntryBySimplifiedOrTraditional(text, text, ::mapDictionary)
            .awaitAsOneOrNull()
    }

    override suspend fun getEntriesBySimplifiedList(simplifiedList: List<String>): List<DictionaryEntry> {
        return database.dictionaryQueries
            .getEntriesBySimplifiedList(simplifiedList, ::mapDictionary)
            .awaitAsList()
    }

    override suspend fun addAll(entries: List<DictionaryEntry>) {
        database.transaction {
            entries.forEach { entry ->
                database.dictionaryQueries.insert(
                    entry.simplified,
                    entry.traditional,
                    entry.pinyin,
                    entry.english,
                )
            }
        }
    }

    override suspend fun deleteAll() {
        database.dictionaryQueries.deleteAll()
    }

    override suspend fun count(): Long {
        return database.dictionaryQueries.count().awaitAsOne()
    }

    private fun mapDictionary(
        id: Long,
        simplified: String,
        traditional: String,
        pinyin: String,
        english: String,
    ): DictionaryEntry = DictionaryEntry(
        id = id,
        simplified = simplified,
        traditional = traditional,
        pinyin = pinyin,
        english = english,
    )
}
