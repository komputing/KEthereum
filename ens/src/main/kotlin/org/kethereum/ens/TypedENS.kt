package org.kethereum.ens

import org.kethereum.eip137.ENSName
import org.kethereum.eip137.toNameHashByteArray
import org.kethereum.ens.generated.ENSRPCConnector
import org.kethereum.ens.generated.ResolverRPCConnector
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC

class TypedENS(private val rpc: EthereumRPC,
               ensAddress: Address = ENS_DEFAULT_CONTRACT_ADDRESS) {

    private val ens = ENSRPCConnector(ensAddress, rpc)

    fun getResolver(name: ENSName) = ens.resolver(name.toNameHashByteArray())

    fun getAddress(name: ENSName): Address? {
        val bytes32 = name.toNameHashByteArray()
        val resolver = ens.resolver(bytes32)
        return if (resolver != null && resolver != ENS_ADDRESS_NOT_FOUND) {
            return ResolverRPCConnector(resolver, rpc).addr(bytes32)
        } else {
            null
        }
    }
}