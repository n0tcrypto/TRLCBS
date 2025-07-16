package com.android.translateappcouchbase.couchbase

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration

object CouchbaseDb {
    lateinit var database: Database

    fun init(context: Context) {
        CouchbaseLite.init(context)

        val config = DatabaseConfiguration() // ✅ FIXED
        database = Database("dictionary", config)
    }
}
