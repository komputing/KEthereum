package org.kethereum.contract.abi.types.model

import org.kethereum.model.Address
import org.walleth.khex.hexToByteArray

class AddressABIType() : BytesABIType(20) {

    constructor(address: Address) : this() {
        value = address.cleanHex.hexToByteArray()
    }

    override fun parseValueFromString(string: String) {
        val tempValue = string.hexToByteArray()
        require(tempValue.size == 20) { "The size of an address must be 20 bytes" }
        value = tempValue
    }
}