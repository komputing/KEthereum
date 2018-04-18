package org.kethereum.erc831

import org.kethereum.erc831.ParseState.*

// as defined in http://eips.ethereum.org/EIPS/eip-831

private enum class ParseState {
    SCHEMA,
    PREFIX,
    PAYLOAD
}

fun String.toERC831() = ERC831().apply {

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

    forEach { char ->
        when {
            char == ':' && currentState == SCHEMA
            -> stateTransition(if (hasPrefix()) PREFIX else PAYLOAD)

            char == '-' && currentState == PREFIX
            -> stateTransition(PAYLOAD)

            else -> currentSegment += char
        }
    }

    if (!currentSegment.isBlank()) {
        stateTransition(PAYLOAD)
    }

}

private fun String.hasPrefix() = contains('-') && (!contains("0x") || indexOf('-') < indexOf("0x"))

fun parseERC681(url: String) = url.toERC831()