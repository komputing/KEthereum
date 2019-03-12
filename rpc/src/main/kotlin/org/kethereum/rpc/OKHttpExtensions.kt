package org.kethereum.rpc

import com.squareup.moshi.JsonAdapter
import java.io.IOException

@Throws(IOException::class)
internal fun <T> JsonAdapter<T>.fromJsonNoThrow(string: String): T? = try {
    fromJson(string)
} catch (e: Exception) {
    null
}