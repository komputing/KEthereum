package org.kethereum.extensions

typealias Stack<T> = MutableList<T>

fun <T> newStack(): Stack<T> = mutableListOf()

/**
 * Pushes item to [Stack]
 * @param item Item to be pushed
 */
fun <T> Stack<T>.push(item: T) = add(item)

/**
 * Pops (removes and return) last item from [Stack]
 * @return item Last item if [Stack] is not empty, null otherwise
 */
fun <T> Stack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null

/**
 * Peeks (return) last item from [Stack]
 * @return item Last item if [Stack] is not empty, null otherwise
 */
fun <T> Stack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null