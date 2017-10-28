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

private val NUMBER_REGEX = Regex("^[0-9]+$")

class ERC67(val url: String) {

    val scheme = url.substringBefore(":")
    private val payload = url.substringAfter(":")

    val addressString = payload.substringBefore("?")
    val query = payload.substringAfter("?", "")
    val queryAsMap = if (query.isBlank()) {
        emptyMap()
    } else {
        query.split("&").map { it.split("=", limit = 2) }.map { it.first() to it[1] }.toMap()
    }
    val address by lazy { Address(getHex()) }

    fun isValid(): Boolean {
        if (scheme != "ethereum" || !url.contains(":")) {
            return false
        }

        if (value != null && !NUMBER_REGEX.matches(value!!)) {
            return false
        }

        if (gas != null && !NUMBER_REGEX.matches(gas!!)) {
            return false
        }

        return true
    }

    fun getHex() = addressString

    val value by lazy { queryAsMap["value"] }
    val gas by lazy { queryAsMap["gas"] }
}