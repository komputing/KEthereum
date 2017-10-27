package org.kethereum.erc55

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.model.SignatureData

class TheEIP155 {

    @Test
    fun canExtractChainIDs() {
        assertThat(SignatureData().copy(v=37).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v=38).extractChainID()).isEqualTo(1)
        assertThat(SignatureData().copy(v=39).extractChainID()).isEqualTo(2)
        assertThat(SignatureData().copy(v=40).extractChainID()).isEqualTo(2)
    }


}