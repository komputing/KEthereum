package org.kethereum.abi_codegen.model

data class GeneratorSpec(
        val classPrefix: String,
        val packageName: String = "",
        val internal: Boolean = true,
        val txDecoderName: String? = classPrefix + "TransactionDecoder",
        val rpcConnectorName: String? = classPrefix + "RPCConnector"
)