package tachiyomi.domain.dictionary.repository

import tachiyomi.domain.dictionary.model.DictionaryEntry

interface DictionaryRepository {
    suspend fun getEntryBySimplified(simplified: String): DictionaryEntry?
    suspend fun getEntriesBySimplifiedList(simplifiedList: List<String>): List<DictionaryEntry>
    suspend fun addAll(entries: List<DictionaryEntry>)
    suspend fun deleteAll()
    suspend fun count(): Long
}
