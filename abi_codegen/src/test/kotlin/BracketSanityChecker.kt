package org.kethereum.abi_codegen

import org.kethereum.extensions.newStack
import org.kethereum.extensions.pop
import org.kethereum.extensions.push

fun String.checkForBracketSanity() {
    val bracketMap = mapOf(
            '(' to ')',
            '{' to '}',
            '[' to ']',
            '<' to '>'
    )
    val reverseBracketMap = bracketMap.map { map -> map.value to map.key }.toMap()
    val bracketStack = newStack<Char>()
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
    if (bracketStack.isNotEmpty()) {
        throw IllegalArgumentException("Not all brackets where closed $bracketStack")
    }
}