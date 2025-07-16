package com.android.translateappcouchbase

import android.app.Application
import com.android.translateappcouchbase.couchbase.CouchbaseDb

class TranslateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CouchbaseDb.init(this) // ✅ Init Couchbase di sini
    }
}
