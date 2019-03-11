package org.kethereum.exceptions

/**
 *
 */
class KeyException(override val message: String?): Throwable(message)

class NoSuchAlgorithmException(override val message: String? = null): Throwable(message)

class NoSuchProviderException(override val message: String? = null): Throwable(message)

class InvalidKeyException(override val message: String?): Throwable(message)