package org.kethereum.crypto.api.ec

import org.kethereum.crypto.model.ECKeyPair

interface KeyPairGenerator {
    fun generate(): ECKeyPair
}
