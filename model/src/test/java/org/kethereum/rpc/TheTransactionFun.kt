
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.functions.*
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import java.math.BigInteger

class TheTransactionFun {

    val someAddress = Address("0xfdf1210fc262c73d0436236a0e07be419babbbc4")
    val anotherAddress = Address("0xfdf1210fc262c73d0436236a0e07be419babbbc7")

    @Test
    fun weCanCreateTokenTransfer() {

        assertThat(createTokenTransferTransactionInput(someAddress, BigInteger("10")).startsWith(tokenTransferSignature)).isTrue()
    }

    @Test
    fun weCanParseTokenTransferValue() {
        val createTokenTransferTransactionInput = Transaction(BigInteger.valueOf(103), someAddress, someAddress, input = createTokenTransferTransactionInput(someAddress, BigInteger("10")))
        assertThat(createTokenTransferTransactionInput.getTokenTransferValue()).isEqualTo(BigInteger("10"))
    }

    @Test
    fun weCanParseTokenTransferTo() {
        val createTokenTransferTransactionInput = Transaction(BigInteger.valueOf(103), someAddress, someAddress, input = createTokenTransferTransactionInput(anotherAddress, BigInteger("10")))
        assertThat(createTokenTransferTransactionInput.getTokenTransferTo()).isEqualTo(anotherAddress)
    }
}
