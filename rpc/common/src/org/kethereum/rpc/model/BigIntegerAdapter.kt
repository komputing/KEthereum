package org.kethereum.rpc.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigInteger


internal class BigIntegerAdapter {

    @ToJson
    fun toJson(card: BigInteger) = card.toString()

    @FromJson
    fun fromJson(card: String) = BigInteger(card)

}