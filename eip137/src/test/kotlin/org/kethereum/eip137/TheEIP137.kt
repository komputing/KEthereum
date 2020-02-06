package org.kethereum.eip137

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.eip137.model.ENSName

/**
 * test-vectors are from here:
 * https://eips.ethereum.org/EIPS/eip-137 and https://docs.ens.domains/contract-api-reference/name-processing
 */
class TheEIP137 {

    @Test
    fun nameHashHexStringWorksForEmpty() {
        assertThat(ENSName("").toNameHash().toHexString()).isEqualTo("0x0000000000000000000000000000000000000000000000000000000000000000")
    }

    @Test
    fun nameHashOnTopLevelNameWorks() {
        assertThat(ENSName("eth").toNameHash().toHexString()).isEqualTo("0x93cdeb708b7545dc668eb9280176169d1c33cfd8ed6f04690a0bcc88a93fc4ae")
    }

    @Test
    fun normalNameHashWorks() {

        assertThat(ENSName("alice.eth").toNameHash().toHexString()).isEqualTo("0x787192fc5378cc32aa956ddfdedbf26b24e8d78e40109add0eea2c1a012c3dec")

        assertThat(ENSName("foo.eth").toNameHash().toHexString()).isEqualTo("0xde9b09fd7c5f901e23a3f19fecc54828e9c848539801e86591bd9801b019f84f")

    }

    @Test
    fun nameHashWithEmojiWorks() {
        // 💩💩💩.eth
        assertThat(ENSName("\uD83D\uDCA9\uD83D\uDCA9\uD83D\uDCA9.eth").toNameHash().toHexString()).isEqualTo("0xa74feb0e5fa5606d3e650275e3bb3873b006a10d558389d3ce2abbe681fcfc8e")
    }

}