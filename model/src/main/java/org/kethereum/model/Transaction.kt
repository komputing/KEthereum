package org.kethereum.model

import org.kethereum.rpc.DEFAULT_GAS_LIMIT
import org.kethereum.rpc.DEFAULT_GAS_PRICE
import java.math.BigInteger

data class Transaction(val value: BigInteger,
                       val from: Address,
                       val to: Address?,

                       val creationEpochSecond: Long? = null,
                       var nonce: Long? = null,
                       var gasPrice: BigInteger = DEFAULT_GAS_PRICE,
                       var gasLimit: BigInteger = DEFAULT_GAS_LIMIT,
                       var error: String? = null,
                       var sigHash: String? = null,
                       var txHash: String? = null,
                       var input: List<Byte> = emptyList(),
                       var unSignedRLP: List<Byte>? = null,
                       var signedRLP: List<Byte>? = null,
                       var eventLog: String? = null)
