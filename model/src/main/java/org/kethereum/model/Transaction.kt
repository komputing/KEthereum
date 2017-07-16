package org.kethereum.model

import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.DEFAULT_GAS_PRICE
import java.math.BigInteger

data class Transaction(val value: BigInteger,
                       val from: Address,
                       val to: Address?,
                       val creationEpochSecond: Long? = null,

                       var nonce: BigInteger? = null,
                       var gasPrice: BigInteger = DEFAULT_GAS_PRICE,
                       var gasLimit: BigInteger = DEFAULT_GAS_LIMIT,
                       var txHash: String? = null,
                       var input: List<Byte> = emptyList(),
                       var signatureData: SignatureData? = null)
