package org.kethereum.eip155

import org.kethereum.crypto.ECKeyPair
import org.kethereum.crypto.getAddress
import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signedMessageToKey
import org.kethereum.functions.encodeRLP
import org.kethereum.model.ChainDefinition
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import java.math.BigInteger.ZERO

/*
*
* http://eips.ethereum.org/EIPS/eip-155
*
*/

/**
 * Signs a transaction via EIP155 using the chainID from ChainDefinition given
 *
 * @return SignatureData - the signature of the transaction signed with the key
 *
 */

fun Transaction.signViaEIP155(key: ECKeyPair, chainDefinition: ChainDefinition): SignatureData {
    val signatureData = key.signMessage(encodeRLP(SignatureData().apply { v = chainDefinition.id.toByte() }))
    return signatureData.copy(v = (signatureData.v + chainDefinition.id * 2 + 8).toByte())
}

/**
 * extracts the ChainID from SignatureData v
 *
 * @return ChainID or null when not EIP155 signed
 *
 */
fun SignatureData.extractChainID() = if (v < 37) { // not EIP 155 signed
    null
} else {
    (v - 35) / 2
}

fun Transaction.extractFrom(eip155signatureData: SignatureData, chainId: Int) =
        getAddress(signedMessageToKey(encodeRLP(SignatureData(ZERO, ZERO, chainId.toByte())), eip155signatureData.copy(v = (eip155signatureData.v - 8 - (chainId * 2)).toByte())))
