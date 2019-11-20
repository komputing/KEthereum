package org.kethereum.example_cli

import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.GeneratorSpec
import org.kethereum.abi_codegen.toKotlinCode
import org.kethereum.eip137.ENSName
import org.kethereum.eip137.toHexString
import org.kethereum.eip137.toNameHash
import org.kethereum.ens.ENS
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.model.Address
import org.kethereum.rpc.HttpEthereumRPC

val rpc = HttpEthereumRPC("https://node3.web3api.com")
val ens = ENS(rpc)

fun main() {
    demoERC55()
    demoEIP137()
    demoENS()
    demoCodeGen()
}

fun demoENS() {
    println("vitalik.eth addr -> " + ens.getAddress(ENSName("vitalik.eth")))
    println("ligi.ethereum.eth node -> " + ENSName("ligi.ethereum.eth").toNameHash().toHexString())
    println("ligi.ethereum.eth github username" + ens.getGithubUserName(ENSName("ligi.ethereum.eth")))

    println("kevins.xyz " + ens.getAddress(ENSName("kevins.xyz")))
}

private fun demoEIP137() {
    println("alice.eth -> " + ENSName("alice.eth").toNameHash().toHexString())
}

private fun demoERC55() {
    val addressNoChecksum = "0x112234455c3a32fd11230c42e7bccd4a84e02010"
    println(addressNoChecksum + "->" + Address(addressNoChecksum).withERC55Checksum())
}

private fun demoCodeGen() {
    EthereumABI(0.javaClass.getResource("/ENSResolver.abi").readText()).toKotlinCode(GeneratorSpec("PeeETH")).writeTo(System.out)
}
