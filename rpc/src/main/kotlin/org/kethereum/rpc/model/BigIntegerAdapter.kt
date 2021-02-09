package org.kethereum.rpc.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


internal class BigIntegerAdapter {

    @ToJson
    fun toJson(card: BigInteger) = card.toString()

    @FromJson
    fun fromJson(card: String) = BigInteger.parseString(card)

}