package org.kethereum.crypto

import org.kethereum.model.secure.SecureRandom

/**
 * Utility class for working with SecureRandom implementation.
 *
 *
 * This is to address issues with SecureRandom on Android. For more information, refer to the
 * following [issue](https://github.com/web3j/web3j/issues/146).
 */
expect object SecureRandomUtils {
    fun secureRandom(): SecureRandom
}
