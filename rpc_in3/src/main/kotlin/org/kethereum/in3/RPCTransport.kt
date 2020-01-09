package org.kethereum.in3

import in3.IN3
import org.kethereum.rpc.BaseEthereumRPC
import org.kethereum.rpc.RPCTransport

class IN3Transport(val in3: IN3) : RPCTransport {
    override fun call(payload: String): String = in3.send(payload).let { result ->
        when {
            result.startsWith("0x") -> """{"result":"$result"}"""
            else  ->  """{"result":$result}"""
        }
    }
}

class IN3RPC(val in3: IN3 = IN3()): BaseEthereumRPC(IN3Transport(in3))
