package org.kethereum.functions.transactions

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.junit.Test
import org.kethereum.functions.encodeRLP
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.createTransactionWithDefaults
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toHexString
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
                val signatureData = SignatureData(
                        r = transactionMap["r"].getBigInteger(),
                        s = transactionMap["s"].getBigInteger(),
                        v = (transactionMap["v"] as String).hexToByteArray().first()

                )
                val transaction = createTransactionWithDefaults(
                        gasLimit = transactionMap["gasLimit"].getBigInteger(),
                        gasPrice = transactionMap["gasPrice"].getBigInteger(),
                        value = transactionMap["value"].getBigInteger(),
                        nonce = transactionMap["nonce"].getBigInteger(),

                        to = Address((transactionMap["to"] as String)),
                        input = (transactionMap["data"] as String).hexToByteArray().toList(),
                        from = Address("0x0"))
                val encodedRLPString = transaction.encodeRLP(signatureData).toHexString()
                if (encodedRLPString != rlp) {
                    throw (Exception("error in " + it + "\n" + rlp + "\n" + encodedRLPString))
                }
            }
        }
    }

    private fun Any?.getBigInteger() = BigInteger((this as String).replace("0x", ""), 16)
}
