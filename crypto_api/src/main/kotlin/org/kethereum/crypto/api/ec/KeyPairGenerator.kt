package org.kethereum.crypto.api.ec

import org.kethereum.model.ECKeyPair

interface KeyPairGenerator {
    fun generate(): ECKeyPair
}
