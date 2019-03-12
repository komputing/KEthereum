package org.kethereum.functions.transactions

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlinx.serialization.parse
import org.kethereum.functions.encodeRLP
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.createTransactionWithDefaults
import org.kethereum.model.extensions.hexToByteArray
import org.kethereum.model.extensions.toHexString
import org.kethereum.model.number.BigInteger
import transactionTestData
import kotlin.test.Test

class TheTransactionEncoder {

    @Test
    @UseExperimental(ImplicitReflectionSerializer::class)
    fun weCanEncodeTransactions() {
        val jsonObject = Json.parse<JsonObject>(transactionTestData)
        jsonObject.keys.forEach {

            val current = jsonObject[it] as JsonObject
            if (current.toMap().containsKey("rlp") && current.toMap().containsKey("transaction")) {
                val rlp = current.toMap().getValue("rlp").content
                val transactionMap = (current["transaction"] as JsonObject).toMap()
                val signatureData = SignatureData(
                        r = transactionMap.getValue("r").content.getBigInteger(),
                        s = transactionMap.getValue("s").content.getBigInteger(),
                        v = (transactionMap.getValue("v").content).hexToByteArray().first()

                )
                val transaction = createTransactionWithDefaults(
                        gasLimit = transactionMap.getValue("gasLimit").content.getBigInteger(),
                        gasPrice = transactionMap.getValue("gasPrice").content.getBigInteger(),
                        value = transactionMap.getValue("value").content.getBigInteger(),
                        nonce = transactionMap.getValue("nonce").content.getBigInteger(),

                        to = Address((transactionMap.getValue("to").content)),
                        input = (transactionMap.getValue("data").content).hexToByteArray().toList(),
                        from = Address("0x0"))
                val encodedRLPString = transaction.encodeRLP(signatureData).toHexString()
                if (encodedRLPString != rlp) {
                    throw (Exception("error in $it\n$rlp\n$encodedRLPString"))
                }
            }
        }
    }

    private fun Any?.getBigInteger() = BigInteger((this.toString()).replace("0x", ""), 16)
}
