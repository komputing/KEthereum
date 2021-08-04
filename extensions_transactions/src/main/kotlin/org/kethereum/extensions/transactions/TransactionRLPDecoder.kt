package org.kethereum.extensions.transactions

import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import org.kethereum.rlp.RLPElement
import org.kethereum.rlp.RLPList
import org.kethereum.rlp.toUnsignedBigIntegerFromRLP
import org.komputing.khex.extensions.toHexString

fun RLPList.toTransaction(): Transaction? {
    require(!(element.size != 6 && element.size != 9)) { "RLPList for transaction must have 6(unsigned) or 9(signed) elements - but has " + element.size }

    val elements: List<RLPElement> = element.map {
        it as? RLPElement ?: throw IllegalArgumentException("RLPList to make transaction must consist only of elements - no list ")
    }

    return Transaction(
        chain = null,
        creationEpochSecond = System.currentTimeMillis(),
        from = null,
        nonce = elements[0].toUnsignedBigIntegerFromRLP(),
        gasPrice = elements[1].toUnsignedBigIntegerFromRLP(),
        gasLimit = elements[2].toUnsignedBigIntegerFromRLP(),
        to = Address(elements[3].bytes.toHexString()),
        value = elements[4].toUnsignedBigIntegerFromRLP(),
        input = elements[5].bytes,
        txHash = null,
        blockNumber = null,
        blockHash = null,
        maxPriorityFeePerGas = null,
        maxFeePerGas = null
    )
}

fun RLPList.toTransactionSignatureData(): SignatureData {
    val elements: List<RLPElement> = element.map {
        it as? RLPElement ?: throw IllegalArgumentException("RLPList to make transaction must consist only of elements - no list ")
    }

    return SignatureData(
        v = elements[6].toUnsignedBigIntegerFromRLP(),
        r = elements[7].toUnsignedBigIntegerFromRLP(),
        s = elements[8].toUnsignedBigIntegerFromRLP()
    )

}