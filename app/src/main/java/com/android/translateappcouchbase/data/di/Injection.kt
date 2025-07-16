package com.android.translateappcouchbase.data.di

import android.content.Context
import com.android.translateappcouchbase.couchbase.CouchbaseDb
import com.android.translateappcouchbase.data.DataRepository

object Injection {

    fun provideRepository(context: Context): DataRepository {
        CouchbaseDb.init(context)
        return DataRepository()
    }

}