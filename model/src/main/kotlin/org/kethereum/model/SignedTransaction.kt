package org.kethereum.model

data class SignedTransaction(
    var transaction: Transaction,
    var signatureData: SignatureData
)