package org.kethereum.flows

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ONE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.kethereum.rpc.EthereumRPC

fun getBlockFlow(rpc: EthereumRPC, delay_between_fetches: Long = 4200) = flow {
    var lastBlock: BigInteger? = null

    while (true) {
        val newBlock = rpc.blockNumber()
        if (newBlock != null && newBlock != lastBlock) {

            while (newBlock != lastBlock) {
                val processedBlock = if (lastBlock == null) newBlock else lastBlock + ONE
                rpc.getBlockByNumber(processedBlock)?.let {
                    emit(it)
                    lastBlock = processedBlock
                }
            }
        }
        delay(delay_between_fetches)
    }
}
