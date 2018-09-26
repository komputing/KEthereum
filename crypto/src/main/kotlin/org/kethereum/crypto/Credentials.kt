package org.kethereum.crypto

import org.walleth.khex.prepend0xPrefix

/**
 * org.kethereum.crypto.Credentials wrapper.
 */
data class Credentials(val ecKeyPair: ECKeyPair?, val address: String?)


fun ECKeyPair.toCredentials(): Credentials {
    val address = getAddress().prepend0xPrefix()
    return Credentials(this, address)
}
