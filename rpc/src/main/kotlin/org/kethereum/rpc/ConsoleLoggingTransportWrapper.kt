package org.kethereum.rpc

class ConsoleLoggingTransportWrapper(private val transport: RPCTransport) : RPCTransport {
    override fun call(payload: String): String? {
        println("> payload: $payload")
        return transport.call(payload).also {
            println("< response: $it")
        }
    }

}