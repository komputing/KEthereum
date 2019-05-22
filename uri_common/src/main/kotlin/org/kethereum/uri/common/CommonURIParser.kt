package org.kethereum.uri.common

import org.kethereum.erc831.ERC831
import org.kethereum.erc831.toERC831
import org.kethereum.model.ChainId
import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.ParseState.*
import java.math.BigInteger

private enum class ParseState {
    ADDRESS,
    CHAIN,
    FUNCTION,
    QUERY
}

fun parseCommonURI(uri: String) = EthereumURI(uri).parseCommonURI()

fun ERC831.parseCommonURI() = let { erc831 ->
    CommonEthereumURIData().apply {
        scheme = erc831.scheme
        prefix = erc831.prefix

        var currentSegment = ""

        var currentState = ADDRESS

        var queryString = ""

        fun stateTransition(newState: ParseState) {
            when (currentState) {
                CHAIN -> chainId = try {
                    ChainId(BigInteger(currentSegment))
                } catch (e: NumberFormatException) {
                    valid = false
                    null
                }
                FUNCTION -> function = currentSegment
                ADDRESS -> address = currentSegment
                QUERY -> queryString = currentSegment
            }
            currentState = newState
            currentSegment = ""
        }


        erc831.payload?.forEach { char ->
            when {
                char == '/' && (currentState == ADDRESS || currentState == CHAIN)
                -> stateTransition(FUNCTION)

                char == '?' && (currentState == ADDRESS || currentState == FUNCTION || currentState == CHAIN)
                -> stateTransition(QUERY)

                char == '@'
                -> stateTransition(CHAIN)

                else -> currentSegment += char
            }
        }

        if (!currentSegment.isBlank()) {
            stateTransition(QUERY)
        }

        query = queryString.split("&")
                .asSequence()
                .filter { it.isNotBlank() }
                .map { it.split("=", limit = 2) }
                .map { it.first() to it.getOrElse(1) { "true" } }
                .toList()

        valid = valid && scheme == "ethereum"
    }
}

fun EthereumURI.parseCommonURI() = toERC831().parseCommonURI()