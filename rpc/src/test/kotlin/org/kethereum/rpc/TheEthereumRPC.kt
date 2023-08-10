package org.kethereum.rpc

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.SignedTransaction
import org.kethereum.rpc.model.BlockInformation
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.math.BigInteger
import java.math.BigInteger.TEN
import java.math.BigInteger.ZERO
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class TheEthereumRPC {

    private val server = MockWebServer()
    private val tested by lazy { HttpEthereumRPC(baseURL = server.url("").toString()) }

    @Before
    fun runBeforeEveryTest() {
        server.start()
    }

    @After
    fun runAfterEveryTest() {
        server.shutdown()
    }

    @Test
    fun get1559TxByHashWorks() {
        val response =
            """{"jsonrpc":"2.0","id":1,"result":{"blockHash":null,"blockNumber":null,"from":"0x694b63b2e053a0c93074795a1025869576389b70","gas":"0x5208","gasPrice":null,"maxFeePerGas":"0x7","maxPriorityFeePerGas":"0x8","hash":"0xcf18a201cfa5452556ad2735430a01de2238b3c15d8c2851d769afd74239fe9b","input":"0x","nonce":"0x0","to":"0x381e247bef0ebc21b6611786c665dd5514dcc31f","transactionIndex":null,"value":"0x0","type":"0x2","accessList":[],"chainId":"0x5","v":"0x1","r":"0x4feeb323bc4e5092f560e5d483203e22d3370eb8ebd1945739eb31fe8e61a21","s":"0x3ceab6d1e6dc551bbeb04f11d62d94cf4c2d792c40a96e82ed5bc9f37391614d"}}"""
        server.enqueue(MockResponse().setBody(response))

        val tx = tested.getTransactionByHash("0x0")
        assertThat(tx).isNotNull
        assertThat(tx!!.transaction.from).isEqualTo(Address("0x694b63b2e053a0c93074795a1025869576389b70"))
        assertThat(tx.signatureData.s).isEqualTo(HexString("0x3ceab6d1e6dc551bbeb04f11d62d94cf4c2d792c40a96e82ed5bc9f37391614d").hexToBigInteger())
        assertThat(tx.transaction.maxFeePerGas).isEqualTo(BigInteger.valueOf(7L))
        assertThat(tx.transaction.maxPriorityFeePerGas).isEqualTo(BigInteger.valueOf(8L))
//        assertThat(tx.transaction.type).isEqualTo(2)
    }

    @Test
    fun getBalanceWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0x0234c8a3397aab58\"}\n"
        server.enqueue(MockResponse().setBody(response))

        assertThat(tested.getBalance(Address("0x0"), "latest")).isEqualTo(HexString("0x0234c8a3397aab58").hexToBigInteger())
    }

    @Test
    fun sendTxErrorWorks() {
        //language=JSON
        val response =
            "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32010,\"message\":\"the method eth_feeHistory does not exist/is not available\"},\"id\":1,\"in3\":{\"lastValidatorChange\":68593,\"lastNodeList\":21988,\"execTime\":76}}\n"
        server.enqueue(MockResponse().setBody(response))

        assertFails("Transaction with the same hash was already imported.") {
            tested.getFeeHistory(10)
        }
    }

    @Test
    fun feeHistoryErrorThrows() {
        //language=JSON
        val response =
            "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32010,\"message\":\"Transaction with the same hash was already imported.\"},\"id\":1,\"in3\":{\"lastValidatorChange\":68593,\"lastNodeList\":21988,\"execTime\":76}}\n"
        server.enqueue(MockResponse().setBody(response))

        assertFails("Transaction with the same hash was already imported.") {
            tested.sendRawTransaction("0x0")
        }
    }

    @Test
    fun errorCodeIsCapturedCorrectly() {
        //language=JSON
        val response =
            "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32010,\"message\":\"Transaction with the same hash was already imported.\"},\"id\":1,\"in3\":{\"lastValidatorChange\":68593,\"lastNodeList\":21988,\"execTime\":76}}\n"
        server.enqueue(MockResponse().setBody(response))

        try {
            tested.sendRawTransaction("0x0")
            throw(IllegalStateException("Exception was not thrown - but it should have"))
        } catch (rpcException: EthereumRPCException) {
            if (rpcException.code != -32010) {
                throw(IllegalArgumentException("The wrong code was captured - expecting -32010 - but got ${rpcException.code}"))
            }
        }

    }

    @Test
    fun sendRawTransactionWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d15273311\"}\n"
        server.enqueue(MockResponse().setBody(response))

        assertThat(tested.sendRawTransaction("0x00")).isEqualTo("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d15273311")
    }

    @Test
    fun getBlockNumberWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0x4299d\"}\n"
        server.enqueue(MockResponse().setBody(response))

        assertThat(tested.blockNumber()).isEqualTo(HexString("0x4299d").hexToBigInteger())
    }

    @Test
    fun getTransactionByHashWorks() {
        val response =
            """{"jsonrpc":"2.0","result":{"blockHash":"0x636ee9bcf20f702a89978881af48bcd34a8d1a00d704d7d1248e46238c2ca084","blockNumber":"0x3ef57","chainId":"0x5","condition":null,"creates":null,"from":"0x03e0ffece04d779388b7a1d5c5102ac54bd479ee","gas":"0x186a0","gasPrice":"0x4a817c800","hash":"0x6fead4befd1d2b69f1aa39a0f43ff9c3d4c5f3953ae1071127209d5608fe3fb7","input":"0x40c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea0100000","nonce":"0x15a","publicKey":"0x6116a22e4e11ee8aed33dd03eacc2eea3d0cab7a2f5be1b792b8c1d0669d13d8642d29c8d2a6efb13f659045593c05bb430776e956bb24a29d7b6f07365724fd","r":"0x59b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eb","raw":"0xf8ac82015a8504a817c800830186a0947af963cf6d228e564e2a0aa0ddbf06210b38615d80b84440c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea01000002ea059b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eba011993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","s":"0x11993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","standardV":"0x1","to":"0x7af963cf6d228e564e2a0aa0ddbf06210b38615d","transactionIndex":"0x0","v":"0x2e","value":"0x0"},"id":1}
"""
        server.enqueue(MockResponse().setBody(response))

        val transaction: SignedTransaction? = tested.getTransactionByHash("0x1234")

        assertThat(transaction?.transaction?.chain).isEqualTo(5L)
        assertThat(transaction?.transaction?.from).isEqualTo(Address("0x03e0ffece04d779388b7a1d5c5102ac54bd479ee"))
        assertThat(transaction?.transaction?.to).isEqualTo(Address("0x7af963cf6d228e564e2a0aa0ddbf06210b38615d"))
        assertThat(transaction?.transaction?.txHash).isEqualTo("0x6fead4befd1d2b69f1aa39a0f43ff9c3d4c5f3953ae1071127209d5608fe3fb7")
        assertThat(transaction?.transaction?.value).isEqualTo(ZERO)

    }

    @Test
    fun getTransactionByHashWorksWhenBlockNumberIsNull() {
        val response =
            """{"jsonrpc":"2.0","result":{"blockHash":"0x636ee9bcf20f702a89978881af48bcd34a8d1a00d704d7d1248e46238c2ca084","blockNumber":null,"chainId":"0x5","condition":null,"creates":null,"from":"0x03e0ffece04d779388b7a1d5c5102ac54bd479ee","gas":"0x186a0","gasPrice":"0x4a817c800","hash":"0x6fead4befd1d2b69f1aa39a0f43ff9c3d4c5f3953ae1071127209d5608fe3fb7","input":"0x40c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea0100000","nonce":"0x15a","publicKey":"0x6116a22e4e11ee8aed33dd03eacc2eea3d0cab7a2f5be1b792b8c1d0669d13d8642d29c8d2a6efb13f659045593c05bb430776e956bb24a29d7b6f07365724fd","r":"0x59b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eb","raw":"0xf8ac82015a8504a817c800830186a0947af963cf6d228e564e2a0aa0ddbf06210b38615d80b84440c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea01000002ea059b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eba011993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","s":"0x11993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","standardV":"0x1","to":"0x7af963cf6d228e564e2a0aa0ddbf06210b38615d","transactionIndex":"0x0","v":"0x2e","value":"0x0"},"id":1}
"""
        server.enqueue(MockResponse().setBody(response))

        val transaction: SignedTransaction? = tested.getTransactionByHash("0x1234")

        assertThat(transaction!!.transaction.blockNumber).isNull()
    }

    @Test
    fun getTransactionByHashWorksWhenBlockHashIsNull() {
        val response =
            """{"jsonrpc":"2.0","result":{"blockHash":null,"blockNumber":null,"chainId":"0x5","condition":null,"creates":null,"from":"0x03e0ffece04d779388b7a1d5c5102ac54bd479ee","gas":"0x186a0","gasPrice":"0x4a817c800","hash":"0x6fead4befd1d2b69f1aa39a0f43ff9c3d4c5f3953ae1071127209d5608fe3fb7","input":"0x40c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea0100000","nonce":"0x15a","publicKey":"0x6116a22e4e11ee8aed33dd03eacc2eea3d0cab7a2f5be1b792b8c1d0669d13d8642d29c8d2a6efb13f659045593c05bb430776e956bb24a29d7b6f07365724fd","r":"0x59b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eb","raw":"0xf8ac82015a8504a817c800830186a0947af963cf6d228e564e2a0aa0ddbf06210b38615d80b84440c10f1900000000000000000000000063ce9f57e2e4b41d3451dec20ddb89143fd755bb000000000000000000000000000000000000000000000016c4abbebea01000002ea059b07e76905fc752ffc1c61c75085bd8c788dbf982ad31e7fc2f27bc3241e0eba011993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","s":"0x11993e81098e5983f6408d2baad340bab97355f83c42a6d71f811d50e3e1f1e9","standardV":"0x1","to":"0x7af963cf6d228e564e2a0aa0ddbf06210b38615d","transactionIndex":"0x0","v":"0x2e","value":"0x0"},"id":1}
"""
        server.enqueue(MockResponse().setBody(response))

        val transaction: SignedTransaction? = tested.getTransactionByHash("0x1234")

        assertThat(transaction!!.transaction.blockHash).isNull()
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

        val blockByNumber: BlockInformation? = tested.getBlockByNumber(TEN)
        assertThat(blockByNumber).isNotNull

        assertThat(blockByNumber?.number).isEqualTo(BigInteger.valueOf(170263L))
        assertThat(blockByNumber?.difficulty).isEqualTo(HexString("0x6cea8018718").hexToBigInteger())

        assertThat(blockByNumber?.extraData).isEqualTo(HexString("0xd783010100844765746887676f312e342e32856c696e7578"))

        assertThat(blockByNumber?.gasLimit).isEqualTo(HexString("0x2fefd8").hexToBigInteger())
        assertThat(blockByNumber?.gasUsed).isEqualTo(HexString("0x5208").hexToBigInteger())

        assertThat(blockByNumber?.hash).isEqualTo(HexString("0x3a72d7691cd720cbb4fffb8145044d3ff5b3c24ef02affaf09d88d0b62c6ba1a"))

        assertThat(blockByNumber?.logsBloom).isEqualTo(HexString("0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"))

        assertThat(blockByNumber?.miner).isEqualTo(HexString("0x8d8dfbd04db0942d79bb1bb038e8876bb67ff825"))
        assertThat(blockByNumber?.nonce).isEqualTo(HexString("0x2e22b4166a4ced0c").hexToBigInteger())

        assertThat(blockByNumber?.parentHash).isEqualTo(HexString("0x7b4f7d83c09f26e8b696aac112b9ba6eea745487725e649431d06f475f81b728"))
        assertThat(blockByNumber?.sha3Uncles).isEqualTo(HexString("0xd7fa711cd3c10f3875fe59be3bd2dbf2d3d071cb2d07a5ef2760b6720b59ebbd"))

        assertThat(blockByNumber?.size).isEqualTo(HexString("0x4ab").hexToBigInteger())
        assertThat(blockByNumber?.stateRoot).isEqualTo(HexString("0x378f69f272e62e09661613cc29873a7875547d3799faf2d83f45898639d23760"))
        assertThat(blockByNumber?.timestamp).isEqualTo(HexString("0x55e4df39").hexToBigInteger())
        assertThat(blockByNumber?.totalDifficulty).isEqualTo(HexString("0x78c0d6f07ec9017").hexToBigInteger())

        assertThat(blockByNumber!!.transactions.size).isEqualTo(1)
        val firstSignedTransaction = blockByNumber.transactions.first()
        val firstTransaction = firstSignedTransaction.transaction
        val firstSignature = firstSignedTransaction.signatureData
        assertThat(firstTransaction.from).isEqualTo(Address("0x867a5221564160c128f2b0ec6b22216c380ddc76"))
        assertThat(firstTransaction.gasLimit).isEqualTo(HexString("0x5208").hexToBigInteger())
        assertThat(firstTransaction.gasPrice).isEqualTo(HexString("0xd4fc47cf6").hexToBigInteger())
        assertThat(firstTransaction.txHash).isEqualTo("0xceebdef59ab3cdde152672014b451f75bb7974b9dca4b30e545b6864d9ffca9d")
        assertThat(firstTransaction.input).isEqualTo(HexString("0x").hexToByteArray())
        assertThat(firstTransaction.nonce).isEqualTo(BigInteger.valueOf(16))
        assertThat(firstTransaction.to).isEqualTo(Address("0x32be343b94f860124dc4fee278fdcbd38c102d88"))
        assertThat(firstTransaction.value).isEqualTo(HexString("0x596c90f09f547400").hexToBigInteger())

        assertThat(firstSignature.v)
            .isEqualTo(HexString("0x1c").hexToBigInteger())

        assertThat(firstSignature.r)
            .isEqualTo(HexString("0xdcd183c34a1ceb7934b7fb32f3169b8f3fff43da936553e4d92ae97bb0a9a765").hexToBigInteger())

        assertThat(firstSignature.s)
            .isEqualTo(HexString("0x76d4be3d62b9e6e6bb8c494c3228f4df31b5c20d8f892fe1d9d35f07afab3d73").hexToBigInteger())
    }

    @Test
    fun accountsWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":[\"0x694b63b2e053a0c93074795a1025869576389b70\"]}\n"
        server.enqueue(MockResponse().setBody(response))

        val expected = listOf(Address("0x694b63b2e053a0c93074795a1025869576389b70"))
        assertThat(tested.accounts()).isEqualTo(expected)
    }

    @Test
    fun signWorks() {
        //language=JSON
        val response = "{\"jsonrpc\":\"2.0\",\"id\":83,\"result\":\"0xa4708243bf782c6769ed04d83e7192dbcf4fc131aa54fde9d889d8633ae39dab03d7babd2392982dff6bc20177f7d887e27e50848c851320ee89c6c63d18ca761c\"}\n"
        server.enqueue(MockResponse().setBody(response))

        val result: SignatureData? = tested.sign(
            address = Address("0x694b63b2e053a0c93074795a1025869576389b70"),
            message = "Hello World".encodeToByteArray()
        )
        assertNotEquals(ZERO, result?.r)
        assertNotEquals(ZERO, result?.s)
        assertNotEquals(ZERO, result?.v)
    }
}