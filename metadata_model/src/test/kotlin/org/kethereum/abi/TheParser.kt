package org.kethereum.abi

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.metadata.model.EthereumMetadataString
import org.kethereum.metadata.parse
import kotlin.test.assertNotNull

class TheParser {

    @Test
    fun canParseMetaData() {
        val tested = EthereumMetadataString(testMetaDataJSON).parse()
        assertThat(tested?.version).isEqualTo(1)
        assertThat(tested?.compiler?.version).isEqualTo("0.5.11+commit.c082d0b4")
        assertThat(tested?.language).isEqualTo("Solidity")

        assertThat(tested?.sources?.size).isEqualTo(1)
        assertThat(tested?.sources?.get("browser/BCY.sol")?.keccak256).isEqualTo("0x87fcd652f317a58050817271574e6e9b3441b4a36eb61fddcca75c8cce2257ef")
        assertThat(tested?.sources?.get("browser/BCY.sol")?.urls?.size).isEqualTo(2)
        assertThat(tested?.output?.abi?.size).isEqualTo(14)
        assertThat(tested?.output?.devdoc?.details).startsWith("Implementation of the")
        assertThat(tested?.output?.devdoc?.methods?.toList()?.size).isEqualTo(11)
        assertThat((tested?.output?.devdoc?.methods?.get("allowance(address,address)") as? Map<String,String>)?.get("details")).isEqualTo("See `IERC20.allowance`.")
        assertThat(tested?.output?.userdoc?.methods?.size).isEqualTo(0)

        assertThat(tested?.settings?.compilationTarget?.get("browser/BCY.sol")).isEqualTo("BitCrystals")
        assertThat(tested?.settings?.optimizer?.runs).isEqualTo(200)
        assertThat(tested?.settings?.optimizer?.enabled).isEqualTo(false)
        assertThat(tested?.settings?.evmVersion).isEqualTo("petersburg")
    }

    @Test
    fun canParseWithConstructor() {
        val tested = EthereumMetadataString(testMetaDataJSONWithConstructor).parse()
        assertThat(tested?.output?.userdoc?.methods?.get("constructor") as String).isEqualTo("Create a new ballot with \$(_numProposals) different proposals.")
    }

    @Test
    fun canParseDevDocsNoDetails() {
        val tested = EthereumMetadataString(testDataDevDocNoDetails).parse()
        assertNotNull(tested)
    }

    @Test
    fun canParseNoOptimizerEnabledField() {
        val tested = EthereumMetadataString(testDataOptimizerSettingsNoEnabledField).parse()
        assertNotNull(tested)
    }

    @Test
    fun canParseWithLargeOptimizerRunsValue() {
        val tested = EthereumMetadataString(testDataLargeOptimizerRunsValue).parse()
        assertNotNull(tested)
    }

    @Test
    fun returnsNullForBadJson() {
        val tested = EthereumMetadataString("yolo").parse()
        assertThat(tested).isNull()
    }
}