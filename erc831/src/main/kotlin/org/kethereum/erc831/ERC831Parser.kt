package org.kethereum.erc831

import org.kethereum.erc831.ParseState.*
import org.kethereum.model.EthereumURI

// as defined in http://eips.ethereum.org/EIPS/eip-831

private enum class ParseState {
    SCHEMA,
    PREFIX,
    PAYLOAD
}

fun EthereumURI.toERC831(): ERC831 = ERC831().apply {

    var currentSegment = ""

    var currentState = SCHEMA

    fun stateTransition(newState: ParseState) {
        when (currentState) {
            SCHEMA -> scheme = currentSegment
            PREFIX -> prefix = currentSegment
            PAYLOAD -> payload = currentSegment
        }
        currentState = newState
        currentSegment = ""
    }

    uri.forEach { char ->
        when {
            char == ':' && currentState == SCHEMA
            -> stateTransition(if (uri.hasPrefix()) PREFIX else PAYLOAD)

            char == '-' && currentState == PREFIX
            -> stateTransition(PAYLOAD)

            else -> currentSegment += char
        }
    }

    if (currentSegment.isNotBlank()) {
        stateTransition(PAYLOAD)
    }

}

private fun String.hasPrefix(): Boolean =
    contains('-') && (!contains("0x") || indexOf('-') < indexOf("0x"))

fun parseERC831(url: String): ERC831 = EthereumURI(url).toERC831()