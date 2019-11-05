package org.kethereum.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class TheByteArrayExtensions {

    @Test
    fun paddingWorks() {
        assertThat(byteArrayOf(2).toFixedLengthByteArray(3)).isEqualTo(byteArrayOf(0, 0, 2))
    }

    @Test
    fun noChangeWhenNotNeeded() {
        assertThat(byteArrayOf(4, 2).toFixedLengthByteArray(2)).isEqualTo(byteArrayOf(4, 2))
    }

    @Test
    fun emptyWorks() {
        assertThat(ByteArray(0)).isEqualTo(byteArrayOf())
    }


    @Test
    fun failsOnTooLargeInput() {
        assertFailsWith(java.lang.IllegalArgumentException::class) {
            byteArrayOf(2, 3).toFixedLengthByteArray(1)
        }
    }

}

