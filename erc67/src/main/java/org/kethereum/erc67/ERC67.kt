package org.kethereum.erc67

import org.kethereum.ETH_IN_WEI
import org.kethereum.model.Address
import java.math.BigDecimal
import java.math.BigInteger
// https://github.com/ethereum/EIPs/issues/67

fun Address.toERC67String() = "ethereum:$hex"
fun Address.toERC67String(valueInWei: BigInteger) = "ethereum:$hex?value=$valueInWei"
fun Address.toERC67String(valueInEther: BigDecimal) = toERC67String((valueInEther * BigDecimal(ETH_IN_WEI)).toBigInteger())

fun String.isERC67String() = startsWith("ethereum:")

class ERC67(val url: String) {

    val scheme = url.substringBefore(":")
    private val payload = url.substringAfter(":")

    val addressString = payload.substringBefore("?")
    val query = payload.substringAfter("?","")
    val queryAsMap = query.split("&").map { it.split("=") }.map { it.first() to it.last() }.toMap()
    val address by lazy { Address(getHex()) }

    fun isValid() = scheme == "ethereum" && url.contains(":")
    fun getHex() = addressString

    fun getValue() = queryAsMap["value"]
}