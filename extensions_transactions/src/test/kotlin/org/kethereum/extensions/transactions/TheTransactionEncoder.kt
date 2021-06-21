package org.kethereum.extensions.transactions

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.Test
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.createTransactionWithDefaults
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString
import transactionTestData
import java.math.BigInteger

class TheTransactionEncoder {

    @Test
    fun weCanEncodeTransactions() {
        val jsonObject = Klaxon().parseJsonObject(transactionTestData.reader())
        jsonObject.keys.forEach {

            val current = jsonObject[it] as JsonObject
            if (current.map.containsKey("rlp") && current.map.containsKey("transaction")) {
                val rlp = current.map["rlp"] as String
                val transactionMap = (current["transaction"] as JsonObject).map
                val signatureData = SignatureData(
                        r = transactionMap["r"].getBigInteger(),
                        s = transactionMap["s"].getBigInteger(),
                        v = transactionMap["v"].getBigInteger()

                )
                val transaction = createTransactionWithDefaults(
                        gasLimit = transactionMap["gasLimit"].getBigInteger(),
                        gasPrice = transactionMap["gasPrice"].getBigInteger(),
                        value = transactionMap["value"].getBigInteger(),
                        nonce = transactionMap["nonce"].getBigInteger(),

                        to = Address((transactionMap["to"] as String)),
                        input = HexString(transactionMap["data"] as String).hexToByteArray(),
                        from = Address("0x0"))
                val encodedRLPString = transaction.encodeLegacyTxRLP(signatureData).toHexString()
                if (encodedRLPString != rlp) {
                    throw (Exception("error in " + it + "\n" + rlp + "\n" + encodedRLPString))
                }
            }
        }
    }

    private fun Any?.getBigInteger() = BigInteger((this as String).replace("0x", ""), 16)
}
