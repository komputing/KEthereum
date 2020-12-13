import io.mockk.confirmVerified
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kethereum.contract.abi.types.encodeTypes
import org.kethereum.contract.abi.types.model.types.AddressETHType
import org.kethereum.contract.abi.types.model.types.StringETHType
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.kethereum.eip137.model.ENSName
import org.kethereum.ens.ENS
import org.kethereum.ens.ENS_DEFAULT_CONTRACT_ADDRESS
import org.kethereum.model.Transaction
import org.kethereum.rpc.EthereumRPC
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString

@ExtendWith(MockKExtension::class)
class TheENS {

    @Test
    fun testGetResolver() {
        val mockRPC = spyk<EthereumRPC>(object : EthereumRPCScaffold() {
            override fun call(transaction: Transaction, block: String) =
                    HexString(AddressETHType.ofNativeKotlinType(TEST_ADDRESSES.first()).paddedValue.toHexString())
        })

        val ens = ENS(mockRPC, ENS_DEFAULT_CONTRACT_ADDRESS)

        assertThat(ens.getResolver(ENSName("foo.eth"))).isEqualTo(TEST_ADDRESSES.first())

        val slot = slot<Transaction>()
        verify(exactly = 1) {
            mockRPC.call(capture(slot))
        }

        assertThat(slot.captured.to).isEqualTo(ENS_DEFAULT_CONTRACT_ADDRESS)
    }

    @Test
    fun testGetAddress() {
        val mockRPC = spyk<EthereumRPC>(object : EthereumRPCScaffold() {
            override fun call(transaction: Transaction, block: String) =
                    HexString(AddressETHType.ofNativeKotlinType(TEST_ADDRESSES.last()).paddedValue.toHexString())
        })

        val ens = ENS(mockRPC, ENS_DEFAULT_CONTRACT_ADDRESS)

        assertThat(ens.getAddress(ENSName("foo.eth"))).isEqualTo(TEST_ADDRESSES.last())

        val slot = mutableListOf<Transaction>()
        verify(exactly = 2) {
            mockRPC.call(capture(slot))
        }

        assertThat(slot.first().to).isEqualTo(ENS_DEFAULT_CONTRACT_ADDRESS)
        assertThat(slot.last().to).isEqualTo(TEST_ADDRESSES.last())

    }

    @Test
    fun testGetTextRecord() {
        val mockRPC = spyk<EthereumRPC>(object : EthereumRPCScaffold() {
            override fun call(transaction: Transaction, block: String) =
                    HexString(encodeTypes(StringETHType.ofNativeKotlinType("my_result")).toHexString())
        })

        val ens = ENS(mockRPC, ENS_DEFAULT_CONTRACT_ADDRESS)

        assertThat(ens.getTextRecord(ENSName("foo.eth"),"test")).isEqualTo("my_result")

        verify(exactly = 2) {
            mockRPC.call(any())
        }
    }
}
