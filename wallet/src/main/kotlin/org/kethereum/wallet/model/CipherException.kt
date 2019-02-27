package org.kethereum.wallet.model

open class CipherException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}

class InvalidPasswordException : CipherException("Invalid password provided")