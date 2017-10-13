package org.kethereum.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigInteger

class TheBigIntegerExtensions {

    @Test
    fun paddingWorks() {
        assertThat(BigInteger("5").toBytesPadded(42).size).isEqualTo(42)
    }

}
