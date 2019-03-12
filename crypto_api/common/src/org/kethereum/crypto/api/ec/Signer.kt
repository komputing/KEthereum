package org.kethereum.crypto.api.ec

import org.kethereum.model.number.BigInteger

interface Signer {
    fun sign(transactionHash: ByteArray, privateKey: BigInteger, canonical: Boolean): ECDSASignature

    fun recover(recId: Int, sig: ECDSASignature, message: ByteArray?): BigInteger?

    fun publicFromPrivate(privateKey: BigInteger): BigInteger
}
