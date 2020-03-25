package org.kethereum.erc1577.model

sealed class ToURIResult
data class SuccessfulToURIResult(val uri: String) : ToURIResult()
sealed class ToURIError : ToURIResult()

class InvalidStorageSystem constructor(private val storageSystem: UInt) : ToURIError() {
    override fun toString() = "Currently only IPFS (0xe3) and SWARM (0xe4) are supported but got " + storageSystem.toString(16)
}

class ContentTooShort(val message: String = "Need at least 6 bytes") : ToURIError()
class InvalidCIDError(private val cid: UInt) : ToURIError() {
    override fun toString() = "Currently only CID 0x01 is supported but got $cid"
}

data class HashLengthError(val expected: Int, val actual: Int) : ToURIError()
data class ContentTypeError(val expected: UInt, val actual: UInt) : ToURIError()