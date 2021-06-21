package org.kethereum.eip155

import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signedMessageToKey
import org.kethereum.crypto.toAddress
import org.kethereum.extensions.transactions.encodeLegacyTxRLP
import org.kethereum.model.ChainId
import org.kethereum.model.ECKeyPair
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.math.BigInteger.valueOf

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

fun Transaction.signViaEIP155(key: ECKeyPair, chainId: ChainId): SignatureData {
    val signatureData = key.signMessage(encodeLegacyTxRLP(SignatureData().apply { v = chainId.value }))
    return signatureData.copy(v = (signatureData.v.plus(chainId.value.shl(1)).plus(valueOf(8))))
}

/**
 * extracts the ChainID from SignatureData v
 *
 * @return ChainID or null when not EIP155 signed
 *
 */
fun SignatureData.extractChainID() = if (v < BigInteger.valueOf(37)) { // not EIP 155 signed
    null
} else {
    (v - valueOf(35)).shr(1)
}

fun Transaction.extractFrom(eip155signatureData: SignatureData, chainId: ChainId) =
        signedMessageToKey(encodeLegacyTxRLP(SignatureData(ZERO, ZERO, chainId.value)), eip155signatureData.copy(v = (eip155signatureData.v - valueOf(8) - (chainId.value * valueOf(2))))).toAddress()
