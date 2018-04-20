package org.kethereum.erc681

import org.kethereum.erc681.ParseState.*
import org.kethereum.erc831.toERC831
import java.math.BigDecimal
import java.math.BigInteger

private val scientificNumberRegEx = Regex("^[0-9]+(\\.[0-9]+)?(e[0-9]+)?$")

private var queryAsList: List<Pair<String, String>> = emptyList()

private enum class ParseState {
    ADDRESS,
    CHAIN,
    FUNCTION,
    QUERY
}

fun String.toERC681() = ERC681().apply {

    val erc831 = toERC831()
    scheme = erc831.scheme
    prefix = erc831.prefix

    var currentSegment = ""

    var currentState = ADDRESS

    var query = ""

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

        return when {
            contains("e") -> {
                val split = split("e")
                BigDecimal(split.first()).multiply(BigDecimal.TEN.pow(split[1].toIntOrNull() ?: 1)).toBigInteger()
            }
            contains(".") -> {
                valid = false
                null
            }
            else -> BigInteger(this)
        }
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

    queryAsList = query.split("&")
            .filter { it.isNotBlank() }
            .map { it.split("=", limit = 2) }
            .map { it.first() to it.getOrElse(1, { "true" }) }

    val queryAsMap = queryAsList.toMap() // should be improved https://github.com/walleth/kethereum/issues/25

    gas = queryAsMap["gas"].toBigInteger()
    value = queryAsMap["value"].toBigInteger()

    functionParams = queryAsList.filter { it.first != "gas" && it.first != "value" }

    valid = valid && scheme == "ethereum"
}

fun parseERC681(url: String) = url.toERC681()