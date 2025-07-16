package com.android.translateappcouchbase.data.utils

import android.util.Log
import kotlin.system.measureTimeMillis

object BenchmarkLogger {
    inline fun <T> logTime(tag: String, actionName: String, block: () -> T): T {
        var result: T
        val elapsed = measureTimeMillis {
            result = block()
        }
        Log.d(tag, "$actionName took $elapsed ms")
        return result
    }
}
