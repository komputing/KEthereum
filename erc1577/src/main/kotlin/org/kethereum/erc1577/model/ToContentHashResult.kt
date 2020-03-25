package org.kethereum.erc1577.model

sealed class ToContentHashResult

data class SuccessfulToContentHashResult(val contentHash: ContentHash) : ToContentHashResult()

sealed class ToContentHashError : ToContentHashResult()

class InvalidSchemeToURIError(private val scheme: String) : ToContentHashError() {
    override fun toString() = "Currently only IPFS (ipfs://) and SWARM (bzz://) are supported but got $scheme"
}

class InvalidSwarmURL(private val content: String) : ToContentHashError() {
    override fun toString() = "swarm (bzz) url must be hex"
}