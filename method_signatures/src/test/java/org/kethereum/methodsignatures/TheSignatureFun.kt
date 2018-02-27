package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.methodsignatures.model.TextMethodSignature

class TheSignatureFun {

    @Test
    fun canCalculateSignature() {
        // from ERC-20
        assertThat(TextMethodSignature("transfer(address,uint256)").toHexSignature().hex).isEqualTo("a9059cbb")
    }

}