package com.android.translateappcouchbase.data

import android.content.Context
import android.util.Log
import com.android.translateappcouchbase.couchbase.CouchbaseDb
import com.android.translateappcouchbase.data.model.WordEntity
import com.android.translateappcouchbase.data.utils.BenchmarkLogger
import com.couchbase.lite.DataSource
import com.couchbase.lite.Document
import com.couchbase.lite.Expression
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository {

    private var cachedWords: List<WordEntity> = emptyList()

    private fun WordEntity.toDocument(): MutableDocument {
        return MutableDocument(word).apply {
            setString("word", word)
            setString("definition", definition)
            setString("note", note)
        }
    }

    private fun Document.toEntity(): WordEntity {
        return WordEntity(
            word = getString("word") ?: "",
            definition = getString("definition") ?: "",
            note = getString("note")
        )
    }


    fun getAllWords(): List<WordEntity> {
        return BenchmarkLogger.logTime("DataRepository", "getAllWords") {
            val query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(CouchbaseDb.database))

            query.execute()
                .mapNotNull { it.getDictionary("dictionary")?.toMap() }
                .mapNotNull { map ->
                    val word = map["word"] as? String
                    val definition = map["definition"] as? String

                    if (word != null && definition != null) {
                        WordEntity(
                            word = word,
                            definition = definition,
                            note = map["note"] as? String
                        )
                    } else {
                        Log.w("CouchbaseParser", "Skipping invalid document: $map")
                        null
                    }
                }
        }
    }


    fun saveBookmark(word: WordEntity) {
        BenchmarkLogger.logTime("DataRepository", "saveBookmark") {
            CouchbaseDb.database.save(word.toDocument())
        }
    }

    fun deleteBookmark(word: String) {
        BenchmarkLogger.logTime("DataRepository", "deleteBookmark") {
            val doc = CouchbaseDb.database.getDocument(word)
            if (doc != null) CouchbaseDb.database.delete(doc)
        }
    }

    fun isWordBookmarked(word: String): Boolean {
        return BenchmarkLogger.logTime("DataRepository", "isWordBookmarked") {
            CouchbaseDb.database.getDocument(word) != null
        }
    }

    fun updateNote(word: String, note: String) {
        BenchmarkLogger.logTime("DataRepository", "updateNote") {
            val doc = CouchbaseDb.database.getDocument(word)
            if (doc != null) {
                val updated = doc.toMutable()
                updated.setString("note", note)
                CouchbaseDb.database.save(updated)
            }
        }
    }

    fun loadWordsFromAsset(context: Context): List<WordEntity> {
        return BenchmarkLogger.logTime("DataRepository", "loadWordsFromAsset") {
            val json = context.assets.open("english_dictionary.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<WordEntity>>() {}.type
            Gson().fromJson(json, type)
        }
    }

    suspend fun searchWordsFromJson(context: Context, query: String): List<WordEntity> {
        return withContext(Dispatchers.IO) {
            BenchmarkLogger.logTime("DataRepository", "searchWordsFromJson") {
                val json = context.assets.open("english_dictionary.json")
                    .bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<WordEntity>>() {}.type
                val allWords: List<WordEntity> = Gson().fromJson(json, type)
                if (query.isBlank()) allWords
                else allWords.filter {
                    it.word.contains(query, ignoreCase = true)
                }
            }
        }

    }
}