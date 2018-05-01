package org.kethereum.wallet.model

class CipherException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}
