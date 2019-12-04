package org.kethereum.contract.abi.types.model.types

import org.kethereum.contract.abi.types.PaginatedByteArray
import org.kethereum.contract.abi.types.leftPadToFixedSize
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.ETH_TYPE_PAGESIZE
import org.kethereum.model.Address
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString

class AddressETHType(override val paddedValue: ByteArray) : ETHType<Address> {

    override fun toKotlinType() = Address(paddedValue.sliceArray(12..31).toHexString())

    constructor(address: Address) : this(ofNativeKotlinType(address).paddedValue)

    companion object {
        fun ofPaginatedByteArray(input: PaginatedByteArray) = input.nextPage()?.let { AddressETHType(it) }

        fun ofNativeKotlinType(input: Address): AddressETHType = AddressETHType(HexString(input.cleanHex).hexToByteArray().leftPadToFixedSize(ETH_TYPE_PAGESIZE))

        fun ofString(input: String) = ofNativeKotlinType(Address(input))
    }

    override fun isDynamic() = false
}