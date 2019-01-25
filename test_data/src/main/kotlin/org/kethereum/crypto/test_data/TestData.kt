package org.kethereum.crypto.test_data

import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey


/**
 * org.kethereum.crypto.Keys generated for unit testing purposes.
 */

const val PRIVATE_KEY_STRING = "a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6"
const val PUBLIC_KEY_STRING = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aab" + "a645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76"
val ADDRESS = Address("0xef678007d18427e6022059dbc264f27507cd1ffc")

val PRIVATE_KEY = PrivateKey(PRIVATE_KEY_STRING)
val PUBLIC_KEY = PublicKey(PUBLIC_KEY_STRING)

val KEY_PAIR = ECKeyPair(PRIVATE_KEY, PUBLIC_KEY)

val TEST_MESSAGE = "A test message".toByteArray()