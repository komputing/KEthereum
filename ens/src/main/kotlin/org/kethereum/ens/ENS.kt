package org.kethereum.ens

import org.kethereum.eip137.model.ENSName
import org.kethereum.eip137.toNameHashByteArray
import org.kethereum.ens.generated.ContentHashResolverRPCConnector
import org.kethereum.ens.generated.ContentHashResolverTransactionGenerator
import org.kethereum.ens.generated.ENSRPCConnector
import org.kethereum.ens.generated.ResolverRPCConnector
import org.kethereum.erc1577.model.ContentHash
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import org.komputing.kethereum.erc181.resolver.ENSReverseResolverRPCConnector
import org.komputing.kethereum.erc634.ERC634RPCConnector

class ENS(private val rpc: EthereumRPC,
          ensAddress: Address = ENS_DEFAULT_CONTRACT_ADDRESS) {

    private val ens by lazy { ENSRPCConnector(ensAddress, rpc) }

    private fun <T> getFromResolver(nameHash: ByteArray, action: (resolver: Address) -> T): T? {
        val resolver = ens.resolver(nameHash)
        return if (resolver != null && resolver != ENS_ADDRESS_NOT_FOUND) {
            return action.invoke(resolver)
        } else {
            null
        }
    }

    /**
     * get the resolver Address for this ENSName
     */
    fun getResolver(name: ENSName) = ens.resolver(name.toNameHashByteArray())

    /**
     * get an Ethereum Address associated with this ENSName
     */
    fun getAddress(name: ENSName) = name.toNameHashByteArray().let {
        getFromResolver(it) { resolver ->
            ResolverRPCConnector(resolver, rpc).addr(it)
        }
    }

    /**
     * get a text record as defined in ERC-634
     * https://github.com/ethereum/EIPs/blob/master/EIPS/eip-634.md
     */
    fun getTextRecord(name: ENSName, key: String) = name.toNameHashByteArray().let {
        getFromResolver(it) { resolver ->
            ERC634RPCConnector(resolver, rpc).text(it, key)
        }
    }

    /**
     * get an e-mail address
     * from EIP-634 text record
     */
    fun getEmail(name: ENSName) = getTextRecord(name, "email")

    /**
     * get an URL
     * from EIP-634 text record
     */
    fun getURL(name: ENSName) = getTextRecord(name, "url")

    /**
     * get an avatar - a URL to an image used as an avatar or logo
     * from EIP-634 text record
     */
    fun getAvatar(name: ENSName) = getTextRecord(name, "avatar")

    /**
     * get a description
     * from EIP-634 text record
     */
    fun getDescription(name: ENSName) = getTextRecord(name, "description")


    /**
     * get a notice
     * from EIP-634 text record
     */
    fun getNotice(name: ENSName) = getTextRecord(name, "notice")

    /**
     * get a list of keywords - A list of keywords, ordered by most significant first; clients that interpresent this field may choose a threshold beyond which to ignore
     * from EIP-634 text record
     */
    fun getKeywords(name: ENSName) = getTextRecord(name, "keywords")?.split(",")

    /**
     * get an github username
     * from EIP-634 text record
     */
    fun getGithubUserName(name: ENSName) = getTextRecord(name, "vnd.github")

    /**
     * get an twitter username
     * from EIP-634 text record
     */
    fun getTwitterUserName(name: ENSName) = getTextRecord(name, "vnd.twitter")

    /**
     * get an peepeth username
     * from EIP-634 text record
     */
    fun getPeepethUserName(name: ENSName) = getTextRecord(name, "vnd.peepeth")


    private fun reverseResolve(name: ENSName) = name.toNameHashByteArray().let {
        getFromResolver(it) { resolver ->
            ENSReverseResolverRPCConnector(resolver, rpc).name(it)
        }
    }

    fun reverseResolve(address: Address) = reverseResolve(ENSName(address.cleanHex.lowercase() + ".addr.reverse"))

    /**
     * get a content hash as defined in ERC-1577
     * https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1577.md
     */
    fun getContentHash(name: ENSName) = name.toNameHashByteArray().let { nameHash ->
        getFromResolver(nameHash) { resolver ->
            ContentHashResolverRPCConnector(resolver, rpc).contenthash(nameHash)?.let {
                ContentHash(it)
            }
        }
    }

    /**
     * get the transaction to set content hash as defined in ERC-1577
     * https://github.com/ethereum/EIPs/blob/master/EIPS/eip-1577.md
     */
    fun getTransactionForSetContentHash(name: ENSName, contentHash: ContentHash) = name.toNameHashByteArray().let { nameHash ->
        getFromResolver(nameHash) { resolver ->
            ContentHashResolverTransactionGenerator(resolver).setContenthash(nameHash, contentHash.byteArray)
        }
    }

}