package org.kethereum.model.exceptions

/**
 *
 */
class KeyException(override val message: String?): Throwable(message) {
    constructor(exception: Throwable): this(exception.message)
}

class NoSuchAlgorithmException(override val message: String? = null): Throwable(message) {
    constructor(exception: Throwable): this(exception.message)
}

class NoSuchProviderException(override val message: String? = null): Throwable(message) {
    constructor(exception: Throwable): this(exception.message)
}

class InvalidKeyException(override val message: String?): Throwable(message) {
    constructor(exception: Throwable): this(exception.message)
}