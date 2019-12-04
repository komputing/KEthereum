package org.kethereum.wallet.data

import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.komputing.khex.extensions.clean0xPrefix
import org.komputing.khex.model.HexString

internal const val PASSWORD = "Insecure Pa55w0rd"
internal const val PRIVATE_KEY_STRING = "a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6"
internal const val PUBLIC_KEY_STRING = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aab" + "a645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76"
internal const val ADDRESS = "0xef678007d18427e6022059dbc264f27507cd1ffc"
internal val ADDRESS_NO_PREFIX = HexString(ADDRESS).clean0xPrefix().string

internal val PRIVATE_KEY = PrivateKey(HexString(PRIVATE_KEY_STRING))
internal val PUBLIC_KEY = PublicKey(HexString(PUBLIC_KEY_STRING))

internal val KEY_PAIR = ECKeyPair(PRIVATE_KEY, PUBLIC_KEY)

internal const val AES_128_CTR_TEST_JSON = """{
    "crypto" : {
        "cipher" : "aes-128-ctr",
        "cipherparams" : {
            "iv" : "02ebc768684e5576900376114625ee6f"
        },
        "ciphertext" : "7ad5c9dd2c95f34a92ebb86740b92103a5d1cc4c2eabf3b9a59e1f83f3181216",
        "kdf" : "pbkdf2",
        "kdfparams" : {
            "c" : 262144,
            "dklen" : 32,
            "prf" : "hmac-sha256",
            "salt" : "0e4cf3893b25bb81efaae565728b5b7cde6a84e224cbf9aed3d69a31c981b702"
        },
        "mac" : "2b29e4641ec17f4dc8b86fc8592090b50109b372529c30b001d4d96249edaf62"
    },
    "id" : "af0451b4-6020-4ef0-91ec-794a5a965b01",
    "version" : 3
}"""

internal const val SCRYPT_TEST_JSON = """{
    "crypto" : {
        "cipher" : "aes-128-ctr",
        "cipherparams" : {
            "iv" : "3021e1ef4774dfc5b08307f3a4c8df00"
        },
        "ciphertext" : "4dd29ba18478b98cf07a8a44167acdf7e04de59777c4b9c139e3d3fa5cb0b931",
        "kdf" : "scrypt",
        "kdfparams" : {
            "dklen" : 32,
            "n" : 262144,
            "r" : 8,
            "p" : 1,
            "salt" : "4f9f68c71989eb3887cd947c80b9555fce528f210199d35c35279beb8c2da5ca"
        },
        "mac" : "7e8f2192767af9be18e7a373c1986d9190fcaa43ad689bbb01a62dbde159338d"
    },
    "id" : "7654525c-17e0-4df5-94b5-c7fde752c9d2",
    "version" : 3
}"""