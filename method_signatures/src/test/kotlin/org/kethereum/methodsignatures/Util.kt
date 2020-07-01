package org.kethereum.methodsignatures

import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi.model.EthereumNamedType
import org.kethereum.abi.model.StateMutability

internal fun createEthereumFun(name: String,
                               payable: Boolean = false,
                               stateMutability: StateMutability = StateMutability.pure,
                               inputs: List<EthereumNamedType> = emptyList(),
                               outputs: List<EthereumNamedType> = emptyList()
) = EthereumFunction(name, payable, stateMutability, inputs, outputs)