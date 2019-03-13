package org.kethereum

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import org.kethereum.model.Address
import org.kethereum.model.extensions.toHexString
import org.kethereum.model.number.BigInteger
import org.kethereum.rpc.model.rpc.TransactionRPC
import org.kethereum.rpc.toKethereumTransaction
import kotlin.test.Test
import kotlin.test.assertEquals

class TheConverter {

    @Test
    @UseExperimental(ImplicitReflectionSerializer::class)
    fun convertingTransactionRPC2KethereumTransactionWorks() {
        //language=JSON
        val transactionRPCJSON = """{"blockHash":"0x652f155c1ee12382c654c0866b41f280481f49aa4335a6d5b8ecaba01f43bcca","blockNumber":"0x6501f","from":"0x9e3d69305da51f34ee29bfb52721e3a824d59e69","gas":"0x3d0900","gasPrice":"0x4a817c800","hash":"0xc7af6af264b8dcc49c321af3dd38f99a8c4afd3b9bab50d24c6d5d1b6dcc160c","input":"0x82ab890a00000000000000000000000000000000000000000000000000000000000027c1","nonce":"0x4327","to":"0xaca0cc3a6bf9552f2866ccc67801d4e6aa6a70f2","transactionIndex":"0x0","value":"0xb","v":"0x1b","r":"0x9d44250ddfd76893ca65a27b74bb91b9d251813dec28154d363f3e5ad1205c22","s":"0x2c18c8e672f60b04dde7ac3db77eef50ead5028f073de8c2cec203d15a40db6f"}"""
        val transactionRPC = Json.parse<TransactionRPC>(transactionRPCJSON)

        val tested = transactionRPC.toKethereumTransaction().transaction

        assertEquals(tested.value, BigInteger("11"))
        assertEquals(tested.input.toHexString(), "0x82ab890a00000000000000000000000000000000000000000000000000000000000027c1")
        assertEquals(tested.from, Address("0x9e3d69305da51f34ee29bfb52721e3a824d59e69"))
        assertEquals(tested.to, Address("0xaca0cc3a6bf9552f2866ccc67801d4e6aa6a70f2"))
        assertEquals(tested.gasLimit, BigInteger("3d0900",16))
        assertEquals(tested.gasPrice, BigInteger("4a817c800",16))
        assertEquals(tested.nonce, BigInteger("4327",16))
        assertEquals(tested.txHash, "0xc7af6af264b8dcc49c321af3dd38f99a8c4afd3b9bab50d24c6d5d1b6dcc160c")
    }

}