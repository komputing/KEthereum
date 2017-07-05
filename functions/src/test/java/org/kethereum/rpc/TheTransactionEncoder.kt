
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.functions.encodeRLP
import org.kethereum.functions.hexToByteArray
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction
import java.math.BigInteger

class TheTransactionEncoder {

    // from https://github.com/ethereum/tests/blob/develop/TransactionTests/ttTransactionTest.json
    @Test
    fun weCanEncodeTransactions() {
        val encoded=Transaction(
                from = Address("0x0"),
                gasLimit = BigInteger("5208", 16),
                gasPrice = BigInteger("1"),
                nonce = 0L,
                value = BigInteger("a", 16),
                to = Address("0x000000000000000000000000000b9331677e6ebf"),
                signatureData = SignatureData(
                        r = BigInteger("98ff921201554726367d2be8c804a7ff89ccf285ebc57dff8ae4c44b9c19ac4a", 16),
                        s = BigInteger("8887321be575c8095f789dd4c743dfe42c1820f9231f98a962b210e3ac2452a3", 16),
                        v = 0x1c.toByte()

                )
        ).encodeRLP()

        assertThat(encoded).isEqualTo("0xf85f800182520894000000000000000000000000000b9331677e6ebf0a801ca098ff921201554726367d2be8c804a7ff89ccf285ebc57dff8ae4c44b9c19ac4aa08887321be575c8095f789dd4c743dfe42c1820f9231f98a962b210e3ac2452a3".hexToByteArray())
    }
}
