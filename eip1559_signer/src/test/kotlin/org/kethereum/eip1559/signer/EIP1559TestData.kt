package org.kethereum.eip1559.signer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.DEFAULT_GAS_PRICE
import org.kethereum.crypto.toECKeyPair
import org.kethereum.eip1559.detector.isEIP1559
import org.kethereum.extensions.transactions.*
import org.kethereum.model.*
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.khex.model.HexString
import java.math.BigInteger

val web3j_test_privateKey = PrivateKey(HexString("4646464646464646464646464646464646464646464646464646464646464646"))
val WEB3J_KEY_PAIR = web3j_test_privateKey.toECKeyPair()


val web3jEIP1559tx = Transaction().apply {
    chain = BigInteger.valueOf(1559)
    nonce = BigInteger.ZERO
    gasPrice = null
    gasLimit = BigInteger.valueOf(30000)
    to = Address("0x627306090abaB3A6e1400e9345bC60c78a8BEf57")
    value = BigInteger.valueOf(123)
    maxPriorityFeePerGas = BigInteger.valueOf(5678)
    maxFeePerGas = BigInteger.valueOf(1100000)
}

val nethereum_privateKey = PrivateKey(HexString("b5b1870957d373ef0eeffecc6e4812c0fd08f554b37b233526acc331bf1544f7"))
val NETHEREUM_KEY_PAIR = nethereum_privateKey.toECKeyPair()

// from https://gist.github.com/juanfranblanco/4cc998247aecd822d6088e382d94a6f1
val nethereumTestTransactions = listOf(
    "1559,0,3,4,3500,,10," to "02f850820617800304820dac800a80c080a0de12484b58bd47130bf9964740b4d68e42bcbbbc39b2eed5b917f0ae66f5e630a01b0d7aa6a810d63c25c115ef217e37023bbe3146b9bb1fe580d004d6432f7f32",

    "1559,2,3,4,3500,0x1ad91ee08f21be3de0ba2ba6918e714da6b45836,10,0x1232" to "02f866820617020304820dac941ad91ee08f21be3de0ba2ba6918e714da6b458360a821232c080a0d5ee3f01ce51d2b2930b268361be6fe9fc542e09311336d335cc4658d7bd7128a0038501925930d090429373c7855220d33a6cb949ea3bea273edcd540271c59ce",

    "0,0,0,0,0,,0," to "02f84c8080808080808080c001a001d4a14026b819394d91fef9336d00d3febed6fbe5d0a993c0d29a3b275c03b6a00cf6961f932346b5e6e5774c063e7a8794cd2dace75464d1fe5f38f3ba744cb5",

    "2,0,3,4,3500,,10," to "02f84e02800304820dac800a80c001a046cfe7dde69e52b91eafd3b213e4547d9ff6294a5ad79383bdb347828fe20102a041e5ab79953b91967bf790a138d9c380d856e6d8b783f1c1751bc446610e6cc6",

    "1559,0,3,0,3500,,0,0x1232" to "02f852820617800380820dac8080821232c001a0ea5637f224ab5b53d4efff652631e42647f3e4a1c539a14864b788c5178ae186a0242e177e38763bf6a0c0c48e9c078814177286367767e09fb362ce49b3577bc3",

    "1559,100,3,4,3500,0x1ad91ee08f21be3de0ba2ba6918e714da6b45836,10,0x1232" to "02f866820617640304820dac941ad91ee08f21be3de0ba2ba6918e714da6b458360a821232c001a04e731e02022a10b97312998630d3dcaabda660e4a5f53d0fc1ebf4ba0cf8597fa01f4639e24823c565e3ac8e094e6eda571d1691022de83285925f9979b8ad7365",

    "1559,2,3,4,3500,0x1ad91ee08f21be3de0ba2ba6918e714da6b45836,10," to "02f864820617020304820dac941ad91ee08f21be3de0ba2ba6918e714da6b458360a80c080a02b7505766dabb65f8ef497955459f9ea43ff4a092153a8acb277321a80b784a8a0276140649dae47bbb8f6d8fdc3e0daddb58bba498aa4e0b8c547d0d8ebdbf9a5"
)

