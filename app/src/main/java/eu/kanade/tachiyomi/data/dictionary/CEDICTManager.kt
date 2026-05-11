package eu.kanade.tachiyomi.data.dictionary

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import okio.source
import tachiyomi.domain.dictionary.model.DictionaryEntry
import tachiyomi.domain.dictionary.repository.DictionaryRepository
import java.io.File
import java.util.zip.ZipInputStream

class CEDICTManager(
    private val context: Context,
    private val repository: DictionaryRepository,
    private val okHttpClient: OkHttpClient
) {

    private val cedictUrl = "https://www.mdbg.net/chinese/export/cedict/cedict_1_0_ts_utf-8_mdbg.zip"
    private val dictionaryFile = File(context.cacheDir, "cedict.zip")

    suspend fun isDictionaryInstalled(): Boolean {
        return repository.count() > 0
    }

    suspend fun installDictionary(onProgress: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            onProgress("Downloading CC-CEDICT...")
            downloadDictionary()
            onProgress("Parsing and Importing...")
            parseAndImport()
            onProgress("Done!")
            dictionaryFile.delete()
        }
    }

    private fun downloadDictionary() {
        val request = Request.Builder().url(cedictUrl).build()
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Failed to download dictionary")
            response.body?.source()?.let { source ->
                dictionaryFile.outputStream().use { output ->
                    source.buffer().readAll(output.sink())
                }
            }
        }
    }

    private suspend fun parseAndImport() {
        repository.deleteAll()
        
        val entries = mutableListOf<DictionaryEntry>()
        ZipInputStream(dictionaryFile.inputStream()).use { zip ->
            val entry = zip.nextEntry
            if (entry != null) {
                val reader = zip.bufferedReader()
                reader.forEachLine { line ->
                    if (line.startsWith("#")) return@forEachLine
                    
                    val parsed = parseLine(line)
                    if (parsed != null) {
                        entries.add(parsed)
                        if (entries.size >= 1000) {
                            repository.addAll(entries.toList())
                            entries.clear()
                        }
                    }
                }
            }
        }
        if (entries.isNotEmpty()) {
            repository.addAll(entries)
        }
    }

    private fun parseLine(line: String): DictionaryEntry? {
        // Format: Traditional Simplified [pinyin] /definition1/definition2/
        try {
            val parts = line.split(" ")
            val traditional = parts[0]
            val simplified = parts[1]
            
            val pinyinStart = line.indexOf("[") + 1
            val pinyinEnd = line.indexOf("]")
            val pinyin = line.substring(pinyinStart, pinyinEnd)
            
            val englishStart = line.indexOf("/")
            val english = line.substring(englishStart).trim('/')
            
            return DictionaryEntry(0, simplified, traditional, pinyin, english)
        } catch (e: Exception) {
            return null
        }
    }
}
