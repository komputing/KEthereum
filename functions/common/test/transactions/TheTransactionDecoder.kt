package org.kethereum.functions.transactions

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlinx.serialization.parse
import org.kethereum.functions.rlp.RLPList
import org.kethereum.functions.rlp.decodeRLP
import org.kethereum.functions.toTransaction
import org.kethereum.functions.toTransactionSignatureData
import org.kethereum.model.Address
import org.kethereum.model.extensions.hexToByteArray
import org.kethereum.model.number.BigInteger
import transactionTestData
import kotlin.test.Test
import kotlin.test.assertEquals

private fun Any?.getBigInteger() = BigInteger((this as String).replace("0x", ""), 16)

class TheTransactionDecoder {

    @Test
    @UseExperimental(ImplicitReflectionSerializer::class)
    fun weCanEncodeTransactions() {
        val jsonObject = Json.parse<JsonObject>(transactionTestData)

        jsonObject.keys.forEach {
            val current = jsonObject[it] as JsonObject

            if (current.toMap().containsKey("rlp") && current.toMap().containsKey("transaction")) {

                try {

                    val rlp = (current["rlp"].primitive.content).hexToByteArray().decodeRLP() as RLPList
                    val transaction = rlp.toTransaction()!!

                    val transactionMap = (current["transaction"] as JsonObject).toMap()
                    assertEquals(transaction.gasLimit, transactionMap.getValue("gasLimit").content.getBigInteger())
                    assertEquals(transaction.gasPrice, transactionMap.getValue("gasPrice").content.getBigInteger())
                    assertEquals(transaction.value, transactionMap.getValue("value").content.getBigInteger())
                    assertEquals(transaction.nonce, transactionMap.getValue("nonce").content.getBigInteger())
                    assertEquals(transaction.to, Address(transactionMap["to"]?.primitive?.content ?: ""))
                    assertEquals(transaction.input, transactionMap["data"]?.primitive?.content?.hexToByteArray()?.toList())

                    val signatureData = rlp.toTransactionSignatureData()
                    assertEquals(signatureData.r, transactionMap.getValue("r").content.getBigInteger() )
                    assertEquals(signatureData.s, transactionMap.getValue("s").content.getBigInteger() )
                    assertEquals(signatureData.v, transactionMap.getValue("v").content.getBigInteger().toByte() )

                } catch (e: Exception) {
                    throw IllegalArgumentException("problem with ${current.toMap()["rlp"]} $e")
                }
            }
        }
    }

}
