package org.kethereum.functions.transactions

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.junit.Test
import org.kethereum.functions.encodeRLP
import org.kethereum.functions.hexToByteArray
import org.kethereum.functions.toHexString
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import transactionTestData
import java.math.BigInteger

class TheTransactionEncoder {

    @Test
    fun weCanEncodeTransactions() {
        val jsonObject = Parser().parse(transactionTestData.reader()) as JsonObject
        jsonObject.keys.forEach {

            val current = jsonObject[it] as JsonObject
            if (current.map.containsKey("rlp") && current.map.containsKey("transaction")) {
                val rlp = current.map["rlp"] as String
                val transactionMap = (current["transaction"] as JsonObject).map
                val transaction = Transaction(
                        gasLimit = transactionMap["gasLimit"].getBigInteger(),
                        gasPrice = transactionMap["gasPrice"].getBigInteger(),
                        value = transactionMap["value"].getBigInteger(),
                        nonce = transactionMap["nonce"].getBigInteger(),

                        to = Address((transactionMap["to"] as String)),
                        input = (transactionMap["data"] as String).hexToByteArray().toList(),
                        from = Address("0x0"),
                        signatureData = SignatureData(
                                r = transactionMap["r"].getBigInteger(),
                                s = transactionMap["s"].getBigInteger(),
                                v = (transactionMap["v"] as String).hexToByteArray().first()

                        ))
                val encodedRLPString = transaction.encodeRLP().toHexString()
                if (encodedRLPString != rlp) {
                    throw (Exception("error in " + it + "\n" + rlp + "\n" + encodedRLPString))
                }
            }
        }
    }

    private fun Any?.getBigInteger() = BigInteger((this as String).replace("0x", ""), 16)
}
