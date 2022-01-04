import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kethereum.eip1559_fee_oracle.suggestEIP1559Fees

import org.kethereum.rpc.HttpEthereumRPC
import java.math.BigInteger
import java.math.BigInteger.ZERO
import kotlin.test.assertEquals

class TheEIP1559FeeOracle {

    @Test
    fun canSignWeb3JEIP1559Transaction() {
        val mockWebServer = MockWebServer()

        mockWebServer.enqueue(
            MockResponse().setBody(
                "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"oldestBlock\":\"0x612e\",\"baseFeePerGas\":[\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\",\"0x7\"],\"gasUsedRatio\":[0.002359253666954271,0.011796717716422203,0,0.004718507333908542,0.002359253666954271,0.011796717716422203,0.00471895671555939,0.002359253666954271,0.004718507333908542,0,0.0070777610008628125,0,0.011796268334771355,0.004718507333908542,0.004718507333908542,0,0,0.0070777610008628125,0,0,0.009437014667817083,0,0.004718507333908542,0.009437014667817083,0.004718507333908542,0.009437014667817083,0.002359253666954271,0.002359253666954271,0,0.002359253666954271,0,0,0.0070777610008628125,0.004718507333908542,0.0070777610008628125,0.0070777610008628125,0.018874478717285016,0.004718507333908542,0.004718507333908542,0.004718507333908542,0.002359253666954271,0,0.011795360758037615,0.00471796457123138,0,0.002359431615574855,0,0.007077396186806234,0,0.02594626991201334,0.004717871211685549,0.00235916024498246,0,0.004717421933406176,0,0.01179355483351544,0,0.011794004111794813,0.009435293145091724,0.002358710966703088,0.004717421933406176,0,0.007076582178388637,0,0.009435293145091724,0,0.002358710966703088,0.009434843866812353,0.004717871211685549,0.004717421933406176,0,0,0.0070757683571962505,0.002358888936806394,0.01887436838738428,0,0,0,0.002358888936806394,0.00792716509257662,0.002358888936806394,0,0,0.002358888936806394,0,0,0.002358888936806394,0,0,0.026478427239664194,0,0.010354785700937267,0,0,0,0.004717328647001322,0.011792647777586108,0.002358888936806394,0,0]}}"
            )
        )

        mockWebServer.enqueue(MockResponse().setBody("""{"jsonrpc":"2.0","id":1,"result":{"oldestBlock":"0x618d","reward":[["0x9502f900"],["0x9502f900"],["0x9502f900"]],"baseFeePerGas":["0x7","0x7","0x7","0x7"],"gasUsedRatio":[0.004717328647001322,0.011792647777586108,0.002358888936806394]}}"""))

        mockWebServer.enqueue(MockResponse().setBody("""{"jsonrpc":"2.0","id":1,"result":{"oldestBlock":"0x6189","reward":[["0x9502f900"]],"baseFeePerGas":["0x7","0x7"],"gasUsedRatio":[0.010354785700937267]}}"""))

        mockWebServer.enqueue(MockResponse().setBody("""{"jsonrpc":"2.0","id":1,"result":{"oldestBlock":"0x6187","reward":[["0x9502f900"]],"baseFeePerGas":["0x7","0x7"],"gasUsedRatio":[0.026478427239664194]}}"""))

        val mockRPC = HttpEthereumRPC(mockWebServer.url("").toString())
        val res = suggestEIP1559Fees(mockRPC)

        assertEquals(16, res.size)
        assertEquals(BigInteger("2500000005"), res.toList().first().second.maxFeePerGas)
    }


}