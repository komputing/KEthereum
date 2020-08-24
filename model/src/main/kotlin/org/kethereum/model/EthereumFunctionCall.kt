package org.kethereum.model

interface EthereumFunctionCall {
    var function: String?
    var functionParams: List<Pair<String, String>>
}