package org.kethereum.in3

import in3.IN3
import org.kethereum.model.ChainId
import org.kethereum.rpc.BaseEthereumRPC
import org.kethereum.rpc.RPCTransport

class IN3Transport : RPCTransport {
    private val in3 by lazy { IN3() }

    fun setChain(chainId: ChainId) {
        in3.chainId = chainId.value.toLong()
    }

    override fun call(payload: String): String = in3.send(payload).let { result ->
        when {
            result.startsWith("0x") -> """{"result":"$result"}"""
            else -> """{"result":$result}"""
        }
    }
}

class IN3RPC(val transport: RPCTransport = IN3Transport()) : BaseEthereumRPC(transport)
