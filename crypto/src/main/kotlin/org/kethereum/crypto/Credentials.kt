package org.kethereum.crypto

import org.kethereum.extensions.hexToBigInteger
import org.walleth.khex.prepend0xPrefix

/**
 * org.kethereum.crypto.Credentials wrapper.
 */
data class Credentials(val ecKeyPair: ECKeyPair?, val address: String?) {

    companion object {

        fun create(ecKeyPair: ECKeyPair): Credentials {
            val address = ecKeyPair.getAddress().prepend0xPrefix()
            return Credentials(ecKeyPair, address)
        }

        fun create(privateKey: String, publicKey: String)
                = create(ECKeyPair(privateKey.hexToBigInteger(), publicKey.hexToBigInteger()))


        fun create(privateKey: String) = create(ECKeyPair.create(privateKey.hexToBigInteger()))

    }
}
