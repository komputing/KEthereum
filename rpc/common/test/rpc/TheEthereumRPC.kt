package org.kethereum.rpc

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.kethereum.model.extensions.hexToBigInteger
import org.kethereum.model.Address
import org.kethereum.rpc.model.BlockInformation
import org.walleth.khex.hexToByteArray
import java.math.BigInteger

class TheEthereumRPC {

    private val server = MockWebServer()
    private val tested by lazy { EthereumRPC(baseURL = server.url("").toString()) }

    @Before
    fun runBeforeEveryTest() {
        server.start()
    }

    @After
    fun runAfterEveryTest() {
        server.shutdown()
    }

    @Test
    fun getBlockNumberWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0x4299d\"}\n"
        server.enqueue(MockResponse().setBody(response))

        assertThat(tested.getBlockNumberString()).isEqualTo("0x4299d")
    }

    @Test
    fun getBlockByNumberWorks() {
        //language=JSON
        val response = """{"jsonrpc": "2.0",
  "id": 2,
  "result": {
    "difficulty": "0x6cea8018718",
    "extraData": "0xd783010100844765746887676f312e342e32856c696e7578",
    "gasLimit": "0x2fefd8",
    "gasUsed": "0x5208",
    "hash": "0x3a72d7691cd720cbb4fffb8145044d3ff5b3c24ef02affaf09d88d0b62c6ba1a",
    "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    "miner": "0x8d8dfbd04db0942d79bb1bb038e8876bb67ff825",
    "mixHash": "0xa3ecdc306bbabde2ef5e148e78474b61b82a3b4ae6cc74d1a779a3c4f81d4d7c",
    "nonce": "0x2e22b4166a4ced0c",
    "number": "0x29917",
    "parentHash": "0x7b4f7d83c09f26e8b696aac112b9ba6eea745487725e649431d06f475f81b728",
    "receiptsRoot": "0xd1a99e841ab3a89aec00381b9b41d3b53c1b490fd1500ec2eddd7f426b9696f1",
    "sha3Uncles": "0xd7fa711cd3c10f3875fe59be3bd2dbf2d3d071cb2d07a5ef2760b6720b59ebbd",
    "size": "0x4ab",
    "stateRoot": "0x378f69f272e62e09661613cc29873a7875547d3799faf2d83f45898639d23760",
    "timestamp": "0x55e4df39",
    "totalDifficulty": "0x78c0d6f07ec9017",
    "transactions": [
      {
        "blockHash": "0x3a72d7691cd720cbb4fffb8145044d3ff5b3c24ef02affaf09d88d0b62c6ba1a",
        "blockNumber": "0x29917",
        "from": "0x867a5221564160c128f2b0ec6b22216c380ddc76",
        "gas": "0x5208",
        "gasPrice": "0xd4fc47cf6",
        "hash": "0xceebdef59ab3cdde152672014b451f75bb7974b9dca4b30e545b6864d9ffca9d",
        "input": "0x",
        "nonce": "0x10",
        "to": "0x32be343b94f860124dc4fee278fdcbd38c102d88",
        "transactionIndex": "0x0",
        "value": "0x596c90f09f547400",
        "v": "0x1c",
        "r": "0xdcd183c34a1ceb7934b7fb32f3169b8f3fff43da936553e4d92ae97bb0a9a765",
        "s": "0x76d4be3d62b9e6e6bb8c494c3228f4df31b5c20d8f892fe1d9d35f07afab3d73"
      }
    ],
    "transactionsRoot": "0x7ab6680c6d2fb21c3ce5aaff81432d377c2a10ed4a1dbf614c04c224c34b5bfc",
    "uncles": [
      "0x5f1972f798964895bf29c82bd6d1117553d4222d609d07a8321eaadecf440f12"]}}"""
        server.enqueue(MockResponse().setBody(response))

        val blockByNumber: BlockInformation? = tested.getBlockByNumber("0x1234")
        assertThat(blockByNumber).isNotNull
        assertThat(blockByNumber!!.transactions.size).isEqualTo(1)
        val firstSignedTransaction = blockByNumber.transactions.first()
        val firstTransaction = firstSignedTransaction.transaction
        val firstSignature = firstSignedTransaction.signatureData
        assertThat(firstTransaction.from).isEqualTo(Address("0x867a5221564160c128f2b0ec6b22216c380ddc76"))
        assertThat(firstTransaction.gasLimit).isEqualTo("0x5208".hexToBigInteger())
        assertThat(firstTransaction.gasPrice).isEqualTo("0xd4fc47cf6".hexToBigInteger())
        assertThat(firstTransaction.txHash).isEqualTo("0xceebdef59ab3cdde152672014b451f75bb7974b9dca4b30e545b6864d9ffca9d")
        assertThat(firstTransaction.input).isEqualTo("0x".hexToByteArray().toList())
        assertThat(firstTransaction.nonce).isEqualTo(BigInteger.valueOf(16))
        assertThat(firstTransaction.to).isEqualTo(Address("0x32be343b94f860124dc4fee278fdcbd38c102d88"))
        assertThat(firstTransaction.value).isEqualTo("0x596c90f09f547400".hexToBigInteger())

        assertThat(firstSignature.v)
                .isEqualTo("0x1c".hexToBigInteger().toByte())

        assertThat(firstSignature.r)
                .isEqualTo("0xdcd183c34a1ceb7934b7fb32f3169b8f3fff43da936553e4d92ae97bb0a9a765".hexToBigInteger())

        assertThat(firstSignature.s)
                .isEqualTo("0x76d4be3d62b9e6e6bb8c494c3228f4df31b5c20d8f892fe1d9d35f07afab3d73".hexToBigInteger())
    }


}