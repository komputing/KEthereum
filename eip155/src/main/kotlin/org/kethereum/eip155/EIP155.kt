package org.kethereum.eip155

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.ZERO
import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signedMessageToKey
import org.kethereum.crypto.toAddress
import org.kethereum.extensions.transactions.encodeRLP
import org.kethereum.model.ChainId
import org.kethereum.model.ECKeyPair
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction

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
    val signatureData = key.signMessage(encodeRLP(SignatureData().apply { v = chainId.value }))
    return signatureData.copy(v = signatureData.v + (chainId.value shl 1) + BigInteger(8))
}

/**
 * extracts the ChainID from SignatureData v
 *
 * @return ChainID or null when not EIP155 signed
 *
 */
fun SignatureData.extractChainID() = if (v < BigInteger(37)) { // not EIP 155 signed
    null
} else {
    (v - BigInteger(35)).shr(1)
}

fun Transaction.extractFrom(eip155signatureData: SignatureData, chainId: ChainId) =
        signedMessageToKey(encodeRLP(SignatureData(ZERO, ZERO, chainId.value)),
            eip155signatureData.copy(v = (eip155signatureData.v - BigInteger(8) - (chainId.value * BigInteger(2))))).toAddress()
