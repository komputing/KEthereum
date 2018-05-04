package org.kethereum.erc961

import com.sun.xml.internal.fastinfoset.vocab.Vocabulary.PREFIX
import org.kethereum.model.Address
import org.kethereum.model.ChainDefinition
import org.kethereum.model.EthereumURI
import org.kethereum.model.Token
import org.kethereum.uri.common.parseCommonURI

class InvalidTokenURIException : IllegalArgumentException("invalid token")

private const val TOKEN_INFO_PREFIX = "token_info"
private const val FULL_URI_PREFIX = "ethereum:$TOKEN_INFO_PREFIX"

fun isEthereumTokenURI(uri: String) = uri.startsWith(FULL_URI_PREFIX)

fun parseTokenFromEthereumURI(uri: String): Token {
    val commonEthereumURI = EthereumURI(uri).parseCommonURI()
    val queryMap = commonEthereumURI.query.toMap()

    if (uri.startsWith("ethereum:$PREFIX")
            || !commonEthereumURI.valid) {
        throw InvalidTokenURIException()
    }

    return Token(
            symbol = queryMap["symbol"] ?: "SYM",
            address = Address(commonEthereumURI.address ?: ""),
            chain = ChainDefinition(commonEthereumURI.chainId ?: 1),
            name = queryMap["name"],
            decimals = queryMap["decimals"]?.toInt() ?: 18,
            type = queryMap["type"]
    )
}

fun EthereumURI.parseToken() = parseTokenFromEthereumURI(uri)
fun EthereumURI.isTokenURI() = isEthereumTokenURI(uri)