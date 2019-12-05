import org.kethereum.model.Address
import org.kethereum.model.ChainId
import org.kethereum.model.SignedTransaction
import org.kethereum.model.Transaction
import org.kethereum.rpc.EthereumRPC
import org.kethereum.rpc.model.BlockInformation
import org.komputing.khex.model.HexString
import java.math.BigInteger

open class EthereumRPCScaffold : EthereumRPC {
    override fun getBlockByNumber(number: BigInteger): BlockInformation? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactionByHash(hash: String): SignedTransaction? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendRawTransaction(data: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun blockNumber(): BigInteger? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun call(transaction: Transaction, block: String): HexString? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gasPrice(): BigInteger? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clientVersion(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun chainId(): ChainId? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStorageAt(address: String, position: String, block: String): HexString? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactionCount(address: String, block: String): BigInteger? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCode(address: String, block: String): HexString? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun estimateGas(transaction: Transaction): BigInteger? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBalance(address: Address, block: String): BigInteger? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}