package org.kethereum.crypto.api.cipher

interface AESCipher {

    fun init(mode: Mode, padding: Padding, operation: Operation, key: ByteArray, iv: ByteArray): AESCipher

    fun performOperation(data: ByteArray): ByteArray

    enum class Mode(val id: String) {
        CTR("CTR")
    }

    enum class Padding(val id: String) {
        NO("NoPadding")
    }

    enum class Operation {
        ENCRYPTION,
        DESCRYPTION
    }
}
