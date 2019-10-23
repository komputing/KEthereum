package org.kethereum.example_cli

import org.kethereum.eip137.ENSName
import org.kethereum.eip137.toHexString
import org.kethereum.eip137.toNameHash
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.model.Address

fun main() {
    demoERC55()
    demoEIP137()
}

private fun demoEIP137() {
    println("alice.eth -> " + ENSName("alice.eth").toNameHash().toHexString())
}

private fun demoERC55() {
    val addressNoChecksum = "0x112234455c3a32fd11230c42e7bccd4a84e02010"
    println(addressNoChecksum + "->" + Address(addressNoChecksum).withERC55Checksum())
}