package org.kethereum.abi_codegen

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TheTypeMap {

    @Test
    fun aliasWorks() {
        assertThat(getType("byte")).isEqualTo(getType("bytes1"))
    }

    @Test
    fun containsAllBytes() {
        (1..32).forEach {
            assertTrue(typeMap.containsKey("byte$it"))
        }
    }


}
