package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse

class TheParser {

    @Test
    fun canParseMetaData() {
        val foo = EthereumMetadataString(testMetaDataJSON).parse()
        assertThat(foo?.version).isEqualTo(1)
        assertThat(foo?.compiler?.version).isEqualTo("0.5.11+commit.c082d0b4")
        assertThat(foo?.language).isEqualTo("Solidity")

        assertThat(foo?.sources?.size).isEqualTo(1)
        assertThat(foo?.sources?.get("browser/BCY.sol")?.keccak256).isEqualTo("0x87fcd652f317a58050817271574e6e9b3441b4a36eb61fddcca75c8cce2257ef")
        assertThat(foo?.sources?.get("browser/BCY.sol")?.urls?.size).isEqualTo(2)
        assertThat(foo?.output?.abi?.size).isEqualTo(14)
        assertThat(foo?.output?.devdoc?.details).startsWith("Implementation of the")
        assertThat(foo?.output?.devdoc?.methods?.toList()?.size).isEqualTo(11)
        assertThat(foo?.output?.devdoc?.methods?.get("allowance(address,address)")?.details).isEqualTo("See `IERC20.allowance`.")
        assertThat(foo?.output?.userdoc?.methods?.size).isEqualTo(0)

        assertThat(foo?.settings?.compilationTarget?.get("browser/BCY.sol")).isEqualTo("BitCrystals")
        assertThat(foo?.settings?.optimizer?.runs).isEqualTo(200)
        assertThat(foo?.settings?.optimizer?.enabled).isEqualTo(false)
        assertThat(foo?.settings?.evmVersion).isEqualTo("petersburg")

    }
}