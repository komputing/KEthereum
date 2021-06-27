package org.kethereum.example_cli

import kotlinx.coroutines.flow.collect
import org.kethereum.abi.EthereumABI
import org.kethereum.abi_codegen.model.GeneratorSpec
import org.kethereum.abi_codegen.toKotlinCode
import org.kethereum.eip137.model.ENSName
import org.kethereum.eip137.toHexString
import org.kethereum.eip137.toNameHash
import org.kethereum.ens.ENS
import org.kethereum.erc55.withERC55Checksum
import org.kethereum.flows.getBlockFlow
import org.kethereum.flows.getTransactionFlow
import org.kethereum.model.Address
import org.kethereum.rpc.EthereumRPC
import org.kethereum.rpc.HttpEthereumRPC
import org.kethereum.rpc.min3.getMin3RPC
import kotlin.system.exitProcess

val rpc =  getMin3RPC()
val ens = ENS(rpc)

suspend fun main() {
    showUsageAndDemoSelect()
}

private suspend fun showUsageAndDemoSelect(appendix: String = "") {
    println("""What should we demo$appendix?
        |
        |ERC555 (1)
        |EIP137 (2)
        |ENS (3)
        |transactionFlow (4)
        |blockFlow (5)
        |CodeGeneration (6)
        |Exit (x)
        """.trimMargin())
    when (readLine()?.uppercase()) {
        "1", "ERC55" -> demoERC55()
        "2", "EIP137" -> demoEIP137()
        "3", "ENS" -> demoENS()
        "4", "TRANSACTIONFLOW" -> {
            getTransactionFlow(rpc).collect { tx ->
                println(tx)
            }
        }
        "5", "BLOCKFLOW" -> {
            getBlockFlow(rpc).collect { block ->
                println(block)
            }
        }
        "6", "CODEGENERATION" -> demoCodeGen()
        "X", "EXIT" -> exitProcess(0)
        else -> println("input not understood")
    }
    showUsageAndDemoSelect(" next")
}

fun demoENS() {
    println("vitalik.eth addr -> " + ens.getAddress(ENSName("vitalik.eth")))
    println("ENS reverse " + ens.reverseResolve(Address("d8da6bf26964af9d7eed9e03e53415d37aa96045")))
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
