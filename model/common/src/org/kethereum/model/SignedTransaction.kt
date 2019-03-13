package org.kethereum.model

import kotlinx.serialization.Serializable

@Serializable
data class SignedTransaction(
        var transaction: Transaction,
        var signatureData: SignatureData
)