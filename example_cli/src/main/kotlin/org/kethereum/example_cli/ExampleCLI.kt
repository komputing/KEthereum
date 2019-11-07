package org.kethereum.example_cli

import org.kethereum.eip137.ENSName
import org.kethereum.eip137.toHexString
import org.kethereum.eip137.toNameHash
import org.kethereum.ens.TypedENS
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.model.Address
import org.kethereum.rpc.HttpEthereumRPC

val rpc = HttpEthereumRPC("https://node3.web3api.com")
val ens = TypedENS(rpc, Address("0x314159265dd8dbb310642f98f50c066173c1259b"))

fun main() {
    demoERC55()
    demoEIP137()
    demoENS()
}

fun demoENS() {
    println("ligi.ethereum.eth -> " + ens.getAddress(ENSName("ligi.ethereum.eth")))
}

private fun demoEIP137() {
    println("alice.eth -> " + ENSName("alice.eth").toNameHash().toHexString())
}

private fun demoERC55() {
    val addressNoChecksum = "0x112234455c3a32fd11230c42e7bccd4a84e02010"
    println(addressNoChecksum + "->" + Address(addressNoChecksum).withERC55Checksum())
}