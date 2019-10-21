package org.kethereum.rpc

interface RPCTransport {
    fun call(payload: String): String?
    fun call(method: String, params: String) = call("""{"jsonrpc":"2.0","method":"$method","params":[$params],"id":1}""")
}