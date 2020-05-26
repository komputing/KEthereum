package org.kethereum.abi

import org.kethereum.abi.model.EthereumABIElement
import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi.model.StateMutability
import org.kethereum.methodsignatures.getHexSignature
import org.kethereum.methodsignatures.model.HexMethodSignature
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import org.kethereum.model.Transaction

fun List<EthereumFunction>.findByTextMethodSignature(signature: TextMethodSignature) =
        firstOrNull { it.toTextMethodSignature() == signature }

fun List<EthereumFunction>.findByHexMethodSignature(signature: HexMethodSignature) =
        firstOrNull { it.toTextMethodSignature().toHexSignature() == signature }

fun Iterable<EthereumABIElement>.getAllFunctions(): List<EthereumFunction> = filter { it.type == "function" }.map {
    it.toEthereumFunction()
}

fun EthereumABIElement.toEthereumFunction() = EthereumFunction(
        name = name ?: throw IllegalArgumentException("A function MUST have a name"),
        inputs = inputs ?: emptyList(),
        outputs = outputs ?: emptyList(),
        payable = payable ?: false,
        stateMutability = stateMutability?: StateMutability.pure
)

fun List<EthereumFunction>.findByTransaction(tx: Transaction) =
        firstOrNull { it.toTextMethodSignature().toHexSignature() == tx.getHexSignature() }
