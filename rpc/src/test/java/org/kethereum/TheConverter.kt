package org.kethereum

import com.squareup.moshi.Moshi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.model.Address
import org.kethereum.rpc.model.TransactionRPC
import org.kethereum.rpc.toKethereumTransaction
import org.walleth.khex.toHexString
import java.math.BigInteger

class TheConverter {
    val transactionRPCAdapter = Moshi.Builder().build().adapter(TransactionRPC::class.java)

    @Test
    fun convertingTransactionRPC2KethereumTransactionWorks() {
        //language=JSON
        val transactionRPCJSON = """{"blockHash":"0x652f155c1ee12382c654c0866b41f280481f49aa4335a6d5b8ecaba01f43bcca","blockNumber":"0x6501f","from":"0x9e3d69305da51f34ee29bfb52721e3a824d59e69","gas":"0x3d0900","gasPrice":"0x4a817c800","hash":"0xc7af6af264b8dcc49c321af3dd38f99a8c4afd3b9bab50d24c6d5d1b6dcc160c","input":"0x82ab890a00000000000000000000000000000000000000000000000000000000000027c1","nonce":"0x4327","to":"0xaca0cc3a6bf9552f2866ccc67801d4e6aa6a70f2","transactionIndex":"0x0","value":"0xb","v":"0x1b","r":"0x9d44250ddfd76893ca65a27b74bb91b9d251813dec28154d363f3e5ad1205c22","s":"0x2c18c8e672f60b04dde7ac3db77eef50ead5028f073de8c2cec203d15a40db6f"}"""
        val transactionRPC = transactionRPCAdapter.fromJson(transactionRPCJSON)!!

        val tested = transactionRPC.toKethereumTransaction()

        assertThat(tested.value).isEqualTo(BigInteger("11"))
        assertThat(tested.input.toHexString()).isEqualTo("0x82ab890a00000000000000000000000000000000000000000000000000000000000027c1")
        assertThat(tested.from).isEqualTo(Address("0x9e3d69305da51f34ee29bfb52721e3a824d59e69"))
        assertThat(tested.to).isEqualTo(Address("0xaca0cc3a6bf9552f2866ccc67801d4e6aa6a70f2"))
        assertThat(tested.gasLimit).isEqualTo(BigInteger("3d0900",16))
        assertThat(tested.gasPrice).isEqualTo(BigInteger("4a817c800",16))
        assertThat(tested.nonce).isEqualTo(BigInteger("4327",16))
        assertThat(tested.txHash).isEqualTo("0xc7af6af264b8dcc49c321af3dd38f99a8c4afd3b9bab50d24c6d5d1b6dcc160c")
    }

}