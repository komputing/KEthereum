package org.kethereum.abi_codegen

import java.util.*

fun String.checkForBracketSanity() {
    val bracketMap = mapOf(
            '(' to ')',
            '{' to '}',
            '[' to ']',
            '<' to '>'
    )
    val reverseBracketMap = bracketMap.map { map -> map.value to map.key }.toMap()
    val bracketStack = Stack<Char>()
    chars().mapToObj { charInt -> charInt.toChar() }.forEach { char ->
        bracketMap[char]?.let {
            bracketStack.push(char)
        }
        reverseBracketMap[char]?.let { reverse ->
            val top = bracketStack.pop()
            if (top != reverse) {
                throw IllegalArgumentException("Closing bracket $char but was never opened")
            }
        }
    }
    if (!bracketStack.isEmpty()) {
        throw IllegalArgumentException("Not all brackets where closed $bracketStack")
    }
}