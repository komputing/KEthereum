package org.kethereum.keystore.api

import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair

interface KeyStore {
    fun addKey(key: ECKeyPair, password: String = "", light: Boolean = false): Address?
    fun deleteKey(address: Address): Boolean
    fun getKeyForAddress(address: Address, password: String = ""): ECKeyPair?
    fun hasKeyForForAddress(address: Address): Boolean
    fun getAddresses(): Set<Address>
}
