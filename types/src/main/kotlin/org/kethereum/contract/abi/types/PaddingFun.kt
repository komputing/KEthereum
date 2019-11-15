package org.kethereum.contract.abi.types

fun ByteArray.leftPadToFixedSize(fixedSize: Int) = if (size == fixedSize) {
    this
} else {
    require(size < fixedSize) { "ByteArray too big - max size is $fixedSize" }
    ByteArray(fixedSize) { getOrNull(size - fixedSize + it) ?: 0 }
}

fun ByteArray.rightPadToFixedSize(fixedSize: Int) = if (size == fixedSize) {
    this
} else {
    require(size < fixedSize) { "ByteArray too big - max size is $fixedSize" }
    ByteArray(fixedSize) { getOrNull(it) ?: 0 }
}

fun ByteArray.rightPadToFixedPageSize(pageSize: Int) = if (size % pageSize == 0) {
    this
} else {
    ByteArray(size + pageSize - size % pageSize) { getOrNull(it) ?: 0 }
}