package org.kethereum.erc681

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.walleth.khex.hexToByteArray

class TheTransactionInputEncoding {

    @Test
    fun weCanEncodeFunctionWithNoParams() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/cast").toTransactionInput()).isEqualTo("96d373e5".hexToByteArray())
    }

    @Test
    fun weCanEncodeFunctionWithOneBooleanParam() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=true").toTransactionInput()).isEqualTo("fc135fee0000000000000000000000000000000000000000000000000000000000000001".hexToByteArray())
    }


    @Test
    fun weCanEncodeDynamicParameter() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/setDatabaseDownloadUrl?string=myLocation").toTransactionInput()).isEqualTo("d3a611500000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000a6d794c6f636174696f6e00000000000000000000000000000000000000000000".hexToByteArray())
    }

}