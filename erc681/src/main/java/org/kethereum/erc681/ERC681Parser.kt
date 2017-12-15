package org.kethereum.erc681

import org.kethereum.erc681.ParseState.*
import java.math.BigInteger

private val scientificNumberRegEx = Regex("^[0-9]+(e[0-9]+)?$")


private var queryAsMap: Map<String, String> = emptyMap()

private enum class ParseState {
    SCHEMA,
    PREFIX,
    ADDRESS,
    CHAIN,
    FUNCTION,
    QUERY
}

fun String.parseERC781() = ERC681().apply {

    var currentSegment = ""

    var currentState = SCHEMA

    fun stateTransition(newState: ParseState) {
        when (currentState) {
            SCHEMA -> scheme = currentSegment
            PREFIX -> prefix = currentSegment
            CHAIN -> chainId = try {
                currentSegment.toLong()
            } catch (e: NumberFormatException) {
                valid = false
                null
            }
            FUNCTION -> function = currentSegment
            ADDRESS -> address = currentSegment
            QUERY -> query = currentSegment
        }
        currentState = newState
        currentSegment = ""
    }


    fun String?.toBigInteger(): BigInteger? {
        if (this == null) {
            return null
        }

        if (!scientificNumberRegEx.matches(this)) {
            valid = false
            return null
        }

        return if (contains("e")) {
            val split = split("e")
            BigInteger(split.first()).multiply(BigInteger.TEN.pow(split[1].toIntOrNull() ?: 1))
        } else {
            BigInteger(this)
        }
    }

    forEach { char ->
        when {
            char == ':' && currentState == SCHEMA
            -> stateTransition(if (contains('-')) PREFIX else ADDRESS)

            char == '-' && currentState == PREFIX
            -> stateTransition(ADDRESS)

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

    queryAsMap = query.split("&")
            .map { it.split("=", limit = 2) }
            .map { it.first() to it.getOrElse(1, { "true" }) }
            .toMap()

    gas = queryAsMap["gas"].toBigInteger()
    value = queryAsMap["value"].toBigInteger()

    valid = valid && scheme == "ethereum"
}

fun parseERC681(url: String) = url.parseERC781()