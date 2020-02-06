package org.kethereum.extensions.transactions

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.rlp.RLPList
import org.kethereum.rlp.decodeRLP
import org.kethereum.model.Address
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import transactionTestData
import java.math.BigInteger

private fun Any?.getBigInteger() = BigInteger((this as String).replace("0x", ""), 16)

class TheTransactionDecoder {

    @Test
    fun weCanEncodeTransactions() {
        val jsonObject = Klaxon().parseJsonObject(transactionTestData.reader())
        jsonObject.keys.forEach {

            val current = jsonObject[it] as JsonObject
            if (current.map.containsKey("rlp") && current.map.containsKey("transaction")) {

                try {

                    val rlp = HexString(current.map["rlp"] as String).hexToByteArray().decodeRLP() as RLPList
                    val transaction = rlp.toTransaction()!!

                    val transactionMap = (current["transaction"] as JsonObject).map
                    assertThat(transaction.gasLimit).isEqualTo(transactionMap["gasLimit"].getBigInteger())
                    assertThat(transaction.gasPrice).isEqualTo(transactionMap["gasPrice"].getBigInteger())
                    assertThat(transaction.value).isEqualTo(transactionMap["value"].getBigInteger())
                    assertThat(transaction.nonce).isEqualTo(transactionMap["nonce"].getBigInteger())
                    assertThat(transaction.to).isEqualTo(Address(transactionMap["to"] as String))
                    assertThat(transaction.input).isEqualTo(HexString(transactionMap["data"] as String).hexToByteArray())

                    val signatureData = rlp.toTransactionSignatureData()
                    assertThat(signatureData.r).isEqualTo(transactionMap["r"].getBigInteger())
                    assertThat(signatureData.s).isEqualTo(transactionMap["s"].getBigInteger())
                    assertThat(signatureData.v).isEqualTo(transactionMap["v"].getBigInteger())

                } catch (e: Exception) {
                    throw IllegalArgumentException("problem with " + current.map["rlp"] + e)
                }
            }
        }
    }

}
