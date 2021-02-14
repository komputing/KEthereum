package org.kethereum.erc681

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import org.kethereum.erc831.ERC831
import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.CommonEthereumURIData
import org.kethereum.uri.common.parseCommonURI

private val scientificNumberRegEx = Regex("^[0-9]+(\\.[0-9]+)?(e[0-9]+)?$")

fun EthereumURI.toERC681() = parseCommonURI().toERC681()

fun ERC831.toERC681() = parseCommonURI().toERC681()

fun CommonEthereumURIData.toERC681() = let { commonURI ->
    ERC681().apply {

        scheme = commonURI.scheme
        prefix = commonURI.prefix
        chainId = commonURI.chainId
        function = commonURI.function
        address = commonURI.address
        valid = commonURI.valid

        fun String?.toBigInteger(): BigInteger? {
            if (this == null) {
                return null
            }

            if (!scientificNumberRegEx.matches(this)) {
                valid = false
                return null
            }

            return when {
                contains("e") -> {
                    val split = split("e")
                    BigDecimal.parseString(split.first()).multiply(BigDecimal.TEN.pow(split[1].toIntOrNull() ?: 1)).toBigInteger()
                }
                contains(".") -> {
                    valid = false
                    null
                }
                else -> BigInteger.parseString(this)
            }
        }

        val queryAsMap = commonURI.query.toMap() // should be improved https://github.com/walleth/kethereum/issues/25

        gasLimit = (queryAsMap["gas"]?:queryAsMap["gasLimit"]).toBigInteger()
        gasPrice = (queryAsMap["gasPrice"]).toBigInteger()
        value = queryAsMap["value"]?.split("-")?.first()?.toBigInteger()

        functionParams = commonURI.query.filter { it.first != "gas" && it.first != "value" }
    }
}

fun parseERC681(url: String) = EthereumURI(url).toERC681()