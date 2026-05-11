package tachiyomi.domain.dictionary.model

data class DictionaryEntry(
    val id: Long,
    val simplified: String,
    val traditional: String,
    val pinyin: String,
    val english: String
)
