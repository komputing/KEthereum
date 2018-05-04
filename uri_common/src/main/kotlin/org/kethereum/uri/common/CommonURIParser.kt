package org.kethereum.uri.common

import org.kethereum.erc831.toERC831
import org.kethereum.model.EthereumURI
import org.kethereum.uri.common.ParseState.*

private enum class ParseState {
    ADDRESS,
    CHAIN,
    FUNCTION,
    QUERY
}

fun parseCommonURI(uri: String) = EthereumURI(uri).parseCommonURI()

fun EthereumURI.parseCommonURI() = CommonEthereumURIData().apply {

    val erc831 = toERC831()
    scheme = erc831.scheme
    prefix = erc831.prefix

    var currentSegment = ""

    var currentState = ParseState.ADDRESS

    var queryString = ""

    fun stateTransition(newState: ParseState) {
        when (currentState) {
            CHAIN -> chainId = try {
                currentSegment.toLong()
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
            .filter { it.isNotBlank() }
            .map { it.split("=", limit = 2) }
            .map { it.first() to it.getOrElse(1, { "true" }) }

    valid = valid && scheme == "ethereum"
}