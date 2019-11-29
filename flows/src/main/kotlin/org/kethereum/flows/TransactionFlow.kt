package org.kethereum.flows

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.kethereum.rpc.EthereumRPC

fun getTransactionFlow(rpc: EthereumRPC, delay_between_fetches: Long = 4200) = flow {
    getBlockFlow(rpc, delay_between_fetches).collect { block ->
        block.transactions.forEach {
            emit(it) // TODO use emitAll(block.transactions.asFlow()) once it is not experimental anymore
        }
    }
}
